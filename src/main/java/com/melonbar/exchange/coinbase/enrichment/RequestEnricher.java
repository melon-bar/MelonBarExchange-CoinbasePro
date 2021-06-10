package com.melonbar.exchange.coinbase.enrichment;

import com.melonbar.exchange.coinbase.annotation.BodyField;
import com.melonbar.exchange.coinbase.annotation.QueryField;
import com.melonbar.exchange.coinbase.annotation.RequestField;
import com.melonbar.exchange.coinbase.rest.api.resource.Resource;
import com.melonbar.exchange.coinbase.exception.InvalidRequestException;
import com.melonbar.exchange.coinbase.http.Http;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;
import com.melonbar.exchange.coinbase.util.AppConfig;
import com.melonbar.exchange.coinbase.util.Format;
import com.melonbar.exchange.coinbase.util.Guard;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

/**
 * Enricher for Coinbase Pro API requests. Handles core responsibilities of populating URI format
 * based on the input implementation of {@link BaseRequest}. During request enrichment, there are two
 * core components that must be in sync: (1) the resource, defined by {@link Resource}, (2) the request
 * path parameters annotated by {@link RequestField} in the extension of {@link BaseRequest}.
 *
 * <p> Given a {@link BaseRequest request}, {@link Http method}, and {@link Resource resource}, enrichment
 * will mutate the input <code>request</code> and populate its: HTTP method, request path, request body,
 * full URI with query string and pagination.
 */
@Slf4j
public class RequestEnricher implements Enricher {

    /** Static {@link ObjectMapper} for json marshalling. */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Suffix for deduping JSON keys.
     */
    private static final String DE_DUPE_SUFFIX_FORMAT = "$DEDUPE_{}$";

    /**
     * Regex string for detecting deduped keys.
     */
    private static final String DE_DUPE_SUFFIX_REGEX = "\\$DEDUPE_\\d\\$";

    /**
     * Internal static record that stores URI params as a pair of index and value. May be sorted by index.
     */
    private static record UriParameter(int index, Object value) implements Comparable<UriParameter> {
        @Override
        public int compareTo(final UriParameter other) {
            return Integer.compare(index, other.index());
        }
    }

    /**
     * Perform enrichment step on the input extension of {@link BaseRequest}. Populates HTTP method, evaluates the URI,
     * and generates the request body based on its set fields.
     *
     * @param request Request
     * @param method HTTP method
     * @param resource Resource authority
     * @param <T> Extension of {@link BaseRequest}
     * @return The same request object passed as input after enrichment.
     */
    @Override
    public <T extends BaseRequest> T enrichRequest(final T request, final Http method, final Resource resource) {
        request.setMethod(method);
        try {
            request.setRequestPath(generateRequestPath(resource, request));
            request.setBody(generateRequestBody(request));
            request.setUri(generateUri(request));
        } catch (IllegalAccessException | JsonProcessingException exception) {
            log.error("Failed to complete request enrichment, current request state: {}", request);
            throw new RuntimeException("Failed to populate URI and/or request body", exception);
        }
        validateRequest(request);
        log.info("For request of type [{}], evaluated method as [{}] and URI as [{}]", request.getClass().getSimpleName(),
                request.getMethod(), request.getUri());
        return request;
    }

    /**
     * Evaluates request path using the request fields (that are annotated with {@link RequestField}) and the
     * corresponding {@link Resource resource authority}. Currently does not support optional parameters.
     *
     * @param resource Resource authority
     * @param request Request whose URI is being evaluated
     * @param <T> Extension of {@link BaseRequest}
     * @return Evaluated URI
     * @throws IllegalAccessException When access to an inaccessible field is attempted
     */
    private <T extends BaseRequest> String generateRequestPath(final Resource resource, final T request)
            throws IllegalAccessException {
        Guard.nonNull(resource, request);
        final String baseRequestPath = generateBaseRequestPath(resource, request);

        // get all fields annotated with @QueryField, including those from inheritance hierarchy
        final Field[] fields = FieldUtils.getFieldsWithAnnotation(request.getClass(), QueryField.class);

        // no query fields, no more work needed
        if (ArrayUtils.isEmpty(fields)) {
            return baseRequestPath;
        }

        // convert to query string
        final StringBuilder queryStringBuilder = new StringBuilder("?");
        for (final Field field : fields) {
            field.setAccessible(true);
            final QueryField queryField = field.getAnnotation(QueryField.class);
            final Optional<Object> maybeValue = Optional.ofNullable(field.get(request));
            maybeValue.ifPresentOrElse(
                    value -> {
                        // do nothing on arrays, currently unsupported
                        if (value.getClass().isArray()) {
                            log.warn("Arrays are unsupported for query field types. Field {} has array type [{}].",
                                    field.getName(), value.getClass().getName());
                            return;
                        }
                        // append to query string builder
                        queryStringBuilder.append(queryField.key())
                                .append("=")
                                .append(value)
                                .append("&");
                    },
                    () -> {
                        // throw error on missing required fields
                        if (queryField.required()) {
                            throw new IllegalStateException(
                                    Format.format("Required query field {} (key={}) for request {} is null!",
                                            field.getName(), queryField.key(), request.getClass().getSimpleName()));
                        }
                    });
        }

        final char tailChar = queryStringBuilder.charAt(queryStringBuilder.length()-1);
        final boolean tailIsDelimitingChar = tailChar == '&' || tailChar == '?';

        // handle pagination
        if (request.getPagination() != null) {
            if (!tailIsDelimitingChar) {
                queryStringBuilder.append('?');
            }
            queryStringBuilder.append(request.getPagination());
        } else if (tailIsDelimitingChar) {
            // remove extraneous ampersand or question mark since nothing needs to be added
            queryStringBuilder.deleteCharAt(queryStringBuilder.length()-1);
        }

        return baseRequestPath + queryStringBuilder;
    }

    /**
     * Generates the base request path provided the {@link Resource} using the input request's members annotated
     * with {@link RequestField}.
     *
     * @param resource {@link Resource}
     * @param request Request
     * @param <T> Extension of {@link BaseRequest}
     * @return Base request path
     */
    private <T extends BaseRequest> String generateBaseRequestPath(final Resource resource, final T request)
            throws IllegalAccessException {
        final Class<? extends BaseRequest> requestClass = request.getClass();
        final UriParameter[] uriParameters = new UriParameter[
                FieldUtils.getFieldsListWithAnnotation(requestClass, RequestField.class).size()];
        String uriFormat = resource.getUri();

        // URI with no params
        if (resource.getMaxArgs() < 1) {
            return uriFormat;
        }

        // track request params and corresponding indices.
        int i = 0;
        for (final Field field : request.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            final RequestField requestField = field.getAnnotation(RequestField.class);

            // ignore fields with no @RequestField annotation
            if (requestField == null) {
                continue;
            }

            // throw error on non-present required values, skip non-present optional values
            final Object value = field.get(request);
            if (value == null) {
                if (requestField.required()) {
                    throw new InvalidRequestException("Found missing request field for index: "
                            + requestField.index());
                } else {
                    // skip
                    continue;
                }
            }

            // append URI parameter, index stored for later sorting
            uriParameters[i++] = new UriParameter(requestField.index(), value);
        }

        // sort in ascending order by index and populate URI
        Arrays.sort(uriParameters);
        return resource.populateRequestPath(Arrays.stream(uriParameters)
                .map(UriParameter::value)
                .toArray(Object[]::new));
    }

    /**
     * Generate request body based on the request class definition. All requests are expected to be an extension of
     * {@link BaseRequest}. All fields, including those inherited, annotated with {@link BodyField} will be processed
     * into the request body.
     *
     * @param request Request that will be used to generate the request body
     * @param <T> Extension of {@link BaseRequest}
     * @return Jsonified request body as string
     * @throws IllegalAccessException When access to an inaccessible field is attempted
     * @throws JsonProcessingException If the {@link ObjectMapper} fails to produce a json format string
     */
    private <T extends BaseRequest> String generateRequestBody(final T request)
            throws IllegalAccessException, JsonProcessingException {
        Guard.nonNull(request);

        // get all fields annotated with @BodyField, including those from inheritance hierarchy
        final Field[] fields = FieldUtils.getFieldsWithAnnotation(request.getClass(), BodyField.class);

        // no body fields, no more work needed
        if (ArrayUtils.isEmpty(fields)) {
            return "";
        }

        // convert request body to string format with deduped keys (if necessary)
        final ObjectNode root = OBJECT_MAPPER.createObjectNode();
        for (final Field field : fields) {
            field.setAccessible(true);
            final BodyField bodyParameter = field.getAnnotation(BodyField.class);
            final Optional<Object> maybeValue = Optional.ofNullable(field.get(request));
            maybeValue.ifPresentOrElse(
                    // body field value present, process
                    value -> {
                        if (value.getClass().isArray()) {
                            /*
                             * Jackson databind does not support duplicate keys in json format, so we must apply temporary
                             * suffix that is known for later de-dupe.
                             */
                            final Object[] values = (Object[]) value;
                            int i = 0;
                            for (final Object _value : values) {
                                root.put(bodyParameter.key() + Format.format(DE_DUPE_SUFFIX_FORMAT, i++),
                                        _value.toString());
                            }
                        } else {
                            // add to object mapper
                            root.put(bodyParameter.key(), value.toString());
                        }
                    },
                    // body field value not present, throw exception if marked as required
                    () -> {
                        if (bodyParameter.required()) {
                            throw new IllegalStateException(
                                    Format.format("Required body field {} (key={}) for request {} is null!",
                                            field.getName(), bodyParameter.key(), request.getClass().getSimpleName()));
                        }
                    });
        }
        final String requestBody = dedupeKeys(OBJECT_MAPPER.writer()
                .writeValueAsString(root)
                .trim());

        log.info("Generated body for request type {}: [{}]", request.getClass().getSimpleName(), requestBody);

        return requestBody;
    }

    /**
     * Prepends the request path with the Coinbase Pro REST endpoint, defined in config.
     *
     * @param request Request
     * @param <T> Extension of {@link BaseRequest}
     * @return Full URI, ready for use in HTTP request
     * @throws IllegalStateException if the input request has no request path set
     */
    private <T extends BaseRequest> String generateUri(final T request) {
        if (request.getRequestPath() == null) {
            throw new IllegalStateException(
                    "Input request has null request path, which is needed to generate the URI.");
        }
        // combine URI components
        return AppConfig.COINBASE_PRO_API_ENDPOINT + request.getRequestPath();
    }

    /**
     * Validate that the request implementation meets its own constraints, which are defined at class-level.
     *
     * @param request Request
     * @param <T> Extension of {@link BaseRequest}
     * @throws InvalidRequestException if <code>T</code>'s implementation of <code>isValidRequest</code> returns false
     */
    private <T extends BaseRequest> void validateRequest(final T request) {
        if (!request.isValidRequest()) {
            throw new InvalidRequestException(Format.format(
                    "Request of type [{}] failed validation check, body: [{}], uri: [{}]",
                    request.getClass().getSimpleName(), request.getBody(), request.getUri()));
        }
    }

    /**
     * Helper for replacing all deduped keys for a given input string, assuming it is in json format. Effectively
     * no-op if the input is null or empty, or if no keys need to be deduped.
     *
     * <p> Consider this input json: </p>
     * <code>
     *     { "key": 1, "key$DEDUPED_0$": 2, "key$DEDUPED_1$": 3, "otherKey": 4 }
     * </code>
     * <p> Once deduped, the resulting json is: </p>
     * <code>
     *     { "key": 1, "key": 2, "key": 3, "otherKey": 4 }
     * </code>
     *
     * @param jsonString Json format string
     * @return Json format string with deduped keys
     */
    private String dedupeKeys(final String jsonString) {
        return StringUtils.isEmpty(jsonString) ? "" : jsonString.replaceAll(DE_DUPE_SUFFIX_REGEX, "");
    }
}
