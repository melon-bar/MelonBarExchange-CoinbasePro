package com.melonbar.exchange.coinbase.aspect;

import com.melonbar.core.http.Http;
import com.melonbar.core.http.request.BaseRequest;
import com.melonbar.core.util.Format;
import com.melonbar.exchange.coinbase.annotation.EnrichRequest;
import com.melonbar.exchange.coinbase.enrichment.Enricher;
import com.melonbar.exchange.coinbase.enrichment.RequestEnricher;
import com.melonbar.exchange.coinbase.rest.api.resource.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

/**
 * Aspect for request enrichment, which will only occur for methods annotated by {@link EnrichRequest} in methods that
 * accept extensions of {@link BaseRequest}, under packages of <code>com.coinbase.api.authenticated.*</code>.
 *
 * <p> TODO: AspectJ not supported for Java 1.16, so this is dead code until it is updated.
 *      Use {@link RequestEnricher} instead.
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class RequestEnrichmentAspect {

    /**
     * Point cut for Coinbase Pro API requests under packages <code>com.coinbase.api.authenticated.*</code> that accept
     * extensions of {@link BaseRequest} as the first param and annotated (method) with {@link EnrichRequest}.
     */
    private static final String REQUEST_POINT_CUT =
            "public * execute(com.coinbase.api.authenticated.*(BaseRequest+)) && @annotation(EnrichRequest)";

    /**
     * Enricher responsible for enriching the input request.
     * TODO: Come up with a way to pass this into an Aspect as singleton without being too hacky.
     */
    private final Enricher enricher;

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
     * @throws Throwable From {@link ProceedingJoinPoint}
     */
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

        enricher.enrichRequest(request, enrichRequestAnnotation.type(), enrichRequestAnnotation.authority());
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
}
