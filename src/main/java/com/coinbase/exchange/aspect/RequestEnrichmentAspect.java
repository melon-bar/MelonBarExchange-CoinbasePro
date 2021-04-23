package com.coinbase.exchange.aspect;

import com.coinbase.exchange.annotation.EnrichRequest;
import com.coinbase.exchange.annotation.RequestField;
import com.coinbase.exchange.api.resource.Resource;
import com.coinbase.exchange.model.request.BaseRequest;
import com.coinbase.exchange.http.Http;
import com.coinbase.exchange.util.Format;
import com.coinbase.exchange.util.Guard;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

/**
 * Aspect for request enrichment before Coinbase Pro API calls. Handles core responsibilities of populating URI format
 * based on the input implementation of {@link BaseRequest}. During request enrichment, there are two core components that
 * must be in sync: (1) the resource authority, defined by {@link Resource}, (2) the URI params annotated by
 * {@link RequestField} in the extension of {@link BaseRequest}.
 *
 * <p> Request enrichment will only occur for methods annotated by {@link EnrichRequest} in methods that accept extensions
 * of {@link BaseRequest}, under packages of <code>com.coinbase.api.authenticated.*</code>.
 */
@Slf4j
@Aspect
public class RequestEnrichmentAspect {

    /**
     * Point cut for Coinbase Pro API requests under packages <code>com.coinbase.api.authenticated.*</code> that accept
     * extensions of {@link BaseRequest} as the first param and annotated (method) with {@link EnrichRequest}.
     */
    private static final String REQUEST_POINT_CUT =
            "public * execute(com.coinbase.api.authenticated.*(BaseRequest+)) && @annotation(EnrichRequest)";

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
     * Enrichment as part of preprocessing step before request dispatch. The join point occurs at the API level, for
     * methods annotated with {@link EnrichRequest}, which contains information on the {@link Resource resource authority}
     * and {@link Http HTTP method} involved in this transaction. Using the annotation-provided information, the request
     * method is set and the URI format is populated based on the {@link Resource resource authority} and the request
     * fields definition defined by the {@link BaseRequest} implementation.
     *
     * <p> Once the URI and method is defined in extension of {@link BaseRequest}, which is done via setters, the real
     * API implementation proceeds.
     *
     * <p> The point cut definition should ensure the following:
     * <ol>
     *     <li> The target method has <code>public</code> accessibility
     *     <li> The target method has at least 1 arg, with the 1st arg being a class that extends {@link BaseRequest}
     *     <li> Target method is annotated with {@link EnrichRequest}
     *     <li> API implementation with the target method is within package <code>com.coinbase.api.authenticated.*</code>
     * </ol>
     *
     * @param joinPoint {@link ProceedingJoinPoint}
     * @param <T> Extension of {@link BaseRequest}
     * @return Result of post-enrichment execution
     * @throws Throwable
     */
    @Around(REQUEST_POINT_CUT)
    public <T extends BaseRequest> Object enrich(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Object[] args = joinPoint.getArgs();
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final EnrichRequest enrichRequestAnnotation = methodSignature.getMethod().getAnnotation(EnrichRequest.class);

        // validate method state
        validateState(methodSignature.getName(), args, enrichRequestAnnotation);

        /*
         * Get request resource authority and HTTP method, and populate URI based on the implementation of
         * the BaseRequest.
         */
        final T request = (T) args[0];
        enrichRequest(request, enrichRequestAnnotation);

        // validate request and proceed
        request.validateRequest();
        args[0] = request;

        return joinPoint.proceed(args);
    }

    /**
     * Sanity check to ensure that all information needed is at an expected state.
     *
     * @param methodName HTTP method name
     * @param args API implementation args
     * @param annotation {@link EnrichRequest}
     */
    private void validateState(final String methodName, final Object[] args, final EnrichRequest annotation) {
        if (args == null || annotation == null || !(args[0] instanceof BaseRequest)) {
            throw new IllegalStateException(Format.format(
                    "Invalid state for enrichment of request for method {} with args: {}, and annotation: {}",
                    methodName, Arrays.toString(args), annotation));
        }
    }

    /**
     * Perform actual enrichment step on the input extension of {@link BaseRequest}. Populates HTTP method and evaluates
     * the URI.
     *
     * @param request Request
     * @param enrichRequestAnnotation {@link EnrichRequest}
     * @param <T> Extension of {@link BaseRequest}
     */
    private <T extends BaseRequest> void enrichRequest(final T request, final EnrichRequest enrichRequestAnnotation) {
        final Resource resource = enrichRequestAnnotation.authority();
        final Http method = enrichRequestAnnotation.type();

        // set method and URI
        request.setMethod(method);
        try {
            request.setUri(getUri(resource, request));
        } catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException("Failed to populate URI template", illegalAccessException);
        }

        log.info("For request of type [{}], evaluated method as [{}] and URI as [{}]", request.getClass().getSimpleName(),
                request.getMethod(), request.getUri());
    }

    /**
     * Evaluates URI for the request using the request fields (that are annotated with {@link RequestField}) and the
     * corresponding {@link Resource resource authority}. Currently does not support optional URI parameters.
     *
     * @param resource Resource authority
     * @param request Request whose URI is being evaluated
     * @param <T> Extension of {@link BaseRequest}
     * @return Evaluated URI
     * @throws IllegalAccessException
     */
    private <T extends BaseRequest> String getUri(final Resource resource, final T request) throws IllegalAccessException {
        Guard.nonNull(resource, request);

        final Class<? extends BaseRequest> requestClass = request.getClass();
        final String uriFormat = resource.getUri();
        final UriParameter[] uriParameters = new UriParameter[requestClass.getAnnotationsByType(RequestField.class).length];

        // URI with no params
        if (resource.getMaxArgs() < 1) {
            return uriFormat;
        }

        // track request params and corresponding indices.
        int i = 0;
        for (final Field field : request.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            final RequestField requestField = field.getAnnotation(RequestField.class);
            final Optional<Object> value = Optional.ofNullable(field.get(request));

            // only handle members with @RequestField annotation, and skip non-present required values
            if (requestField == null || (value.isEmpty() && requestField.required())) {
                continue;
            }

            // append URI parameter, index stored for later sorting
            uriParameters[i++] = new UriParameter(requestField.index(), field.get(request));
        }

        // sort in ascending order by index and populate URI
        Arrays.sort(uriParameters);
        return resource.populateUri(Arrays.stream(uriParameters)
                .map(UriParameter::value)
                .toArray(Object[]::new));
    }
}
