package com.coinbase.aspect;

import com.coinbase.annotation.EnrichRequest;
import com.coinbase.http.Http;
import com.coinbase.model.request.BaseRequest;
import com.coinbase.util.Format;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

@Aspect
public class RequestEnrichmentAspect {

    private static final String REQUEST_POINT_CUT =
            "public * execute(com.coinbase.api.authenticated.*(Request+)) && @annotation(BaseRequest)";

    @Around(REQUEST_POINT_CUT)
    public Object enrich(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Object[] args = joinPoint.getArgs();
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final EnrichRequest enrichRequestAnnotation = methodSignature.getMethod().getAnnotation(EnrichRequest.class);

        // validate
        validateState(methodSignature.getName(), args, enrichRequestAnnotation);

        /*
         * Get request resource authority and HTTP method, and populate URI based on the implementation of
         * the BaseRequest.
         */
        final BaseRequest request = (BaseRequest) args[0];
        enrichRequest(request, enrichRequestAnnotation);

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
        final String uriFormat = enrichRequestAnnotation.authority().getUri();
        final Http method = enrichRequestAnnotation.type();

        request.setMethod(method);
    }
}
