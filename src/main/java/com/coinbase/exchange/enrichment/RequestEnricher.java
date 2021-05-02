package com.coinbase.exchange.enrichment;

import com.coinbase.exchange.annotation.BodyField;
import com.coinbase.exchange.annotation.RequestField;
import com.coinbase.exchange.api.resource.Resource;
import com.coinbase.exchange.exception.InvalidRequestException;
import com.coinbase.exchange.http.Http;
import com.coinbase.exchange.model.request.BaseRequest;
import com.coinbase.exchange.util.Format;
import com.coinbase.exchange.util.Guard;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

/**
 * Enricher for Coinbase Pro API requests. Handles core responsibilities of populating URI format
 * based on the input implementation of {@link BaseRequest}. During request enrichment, there are two core components that
 * must be in sync: (1) the resource authority, defined by {@link Resource}, (2) the URI params annotated by
 * {@link RequestField} in the extension of {@link BaseRequest}.
 */
@Slf4j
public class RequestEnricher implements Enricher {

    /**
     * Coinbase Pro URL.
     */
    private static final String COINBASE_PRO_ENDPOINT = "https://api.pro.coinbase.com";

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
            request.setRequestPath(generateUri(resource, request));
            request.setUri(COINBASE_PRO_ENDPOINT + request.getRequestPath());
            request.setBody(generateRequestBody(request));
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
     * Evaluates URI for the request using the request fields (that are annotated with {@link RequestField}) and the
     * corresponding {@link Resource resource authority}. Currently does not support optional URI parameters.
     *
     * @param resource Resource authority
     * @param request Request whose URI is being evaluated
     * @param <T> Extension of {@link BaseRequest}
     * @return Evaluated URI
     * @throws IllegalAccessException When access to an inaccessible field is attempted
     */
    private <T extends BaseRequest> String generateUri(final Resource resource, final T request)
            throws IllegalAccessException {
        Guard.nonNull(resource, request);

        final Class<? extends BaseRequest> requestClass = request.getClass();
        final UriParameter[] uriParameters = new UriParameter[
                FieldUtils.getFieldsListWithAnnotation(requestClass, RequestField.class).size()];
        String uriFormat = resource.getUri();

        // may be paginated
        if (request.getPagination() != null) {
            uriFormat += request.getPagination().toString();
        }

        // URI with no params
        if (resource.getMaxArgs() < 1) {
            return uriFormat;
        }

        // track request params and corresponding indices.
        int i = 0;
        for (final Field field : request.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            final RequestField requestField = field.getAnnotation(RequestField.class);
            final Object value = field.get(request);

            // only handle members with @RequestField annotation, and skip non-present required values
            if (requestField == null || (value == null && !requestField.required())) {
                continue;
            }

            // append URI parameter, index stored for later sorting
            uriParameters[i++] = new UriParameter(requestField.index(), value);
        }

        // sort in ascending order by index and populate URI
        Arrays.sort(uriParameters);
        return resource.populateUri(Arrays.stream(uriParameters)
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
        final Field[] fields = Arrays.stream(FieldUtils.getAllFields(request.getClass()))
                .filter(field -> field.getAnnotation(BodyField.class) != null)
                .toArray(Field[]::new);

        // no body fields, no more work needed
        if (fields.length == 0) {
            return "";
        }

        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode root = mapper.createObjectNode();

        // process each field
        for (final Field field : fields) {
            field.setAccessible(true);
            final BodyField bodyField = field.getAnnotation(BodyField.class);
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
                                root.put(bodyField.key() + Format.format(DE_DUPE_SUFFIX_FORMAT, i++),
                                        _value.toString());
                            }
                        } else {
                            // add to object mapper
                            root.put(bodyField.key(), value.toString());
                        }
                    },
                    // body field value not present, throw exception if marked as required
                    () -> {
                        if (bodyField.required()) {
                            throw new RuntimeException(Format.format("Required field {} (key={}) for request {} is null!",
                                    field.getName(), bodyField.key(), request.getClass().getSimpleName()));
                        }
                    }
            );
        }

        // convert request body to string format with deduped keys (if necessary)
        final String requestBody = dedupeKeys(mapper.writeValueAsString(root).trim());
        log.info("Generated body for request type {}: [{}]", request.getClass().getSimpleName(), requestBody);

        return requestBody;
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
