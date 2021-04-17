package com.coinbase.aspect;

import com.coinbase.annotation.EnrichRequest;
import com.coinbase.annotation.RequestField;
import com.coinbase.api.resource.Resource;
import com.coinbase.http.Http;
import com.coinbase.model.request.BaseRequest;
import com.coinbase.util.Format;
import com.coinbase.util.Guard;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Aspect
public class RequestEnrichmentAspect {

    private static final String REQUEST_POINT_CUT =
            "public * execute(com.coinbase.api.authenticated.*(Request+)) && @annotation(BaseRequest)";

    private static record UriParameter(int index, Object value) implements Comparable<UriParameter> {
        @Override
        public int compareTo(final UriParameter other) {
            return Integer.compare(index, other.index());
        }
    }

    @Around(REQUEST_POINT_CUT)
    public Object enrich(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Object[] args = joinPoint.getArgs();
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final EnrichRequest enrichRequestAnnotation = methodSignature.getMethod().getAnnotation(EnrichRequest.class);

        // validate method state
        validateState(methodSignature.getName(), args, enrichRequestAnnotation);

        /*
         * Get request resource authority and HTTP method, and populate URI based on the implementation of
         * the BaseRequest.
         */
        final BaseRequest request = (BaseRequest) args[0];
        enrichRequest(request, enrichRequestAnnotation);

        // validate request and proceed
        request.validateRequest();
        return joinPoint.proceed(args);
    }

    private void validateState(final String methodName, final Object[] args, final EnrichRequest annotation) {
        if (args == null || annotation == null || !(args[0] instanceof BaseRequest)) {
            throw new IllegalStateException(Format.format(
                    "Invalid state for enrichment of request for method {} with args: {}, and annotation: {}",
                    methodName, Arrays.toString(args), annotation));
        }
    }

    private void enrichRequest(final BaseRequest request, final EnrichRequest enrichRequestAnnotation) {
        final Resource resource = enrichRequestAnnotation.authority();
        final Http method = enrichRequestAnnotation.type();

        // set method and URI
        request.setMethod(method);
        try {
            request.setUri(getUri(resource, request));
        } catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException("Failed to populate URI template", illegalAccessException);
        }
    }

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
