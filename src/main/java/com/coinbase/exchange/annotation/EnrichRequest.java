package com.coinbase.exchange.annotation;

import com.coinbase.exchange.api.resource.Resource;
import com.coinbase.exchange.aspect.RequestEnrichmentAspect;
import com.coinbase.exchange.http.Http;
import com.coinbase.exchange.model.request.BaseRequest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks API implementation methods that require a pre-processing step to evaluate HTTP method and the URI based on
 * the provided request fields. Expects an extension of {@link BaseRequest} as the first method parameter.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnrichRequest {

    /**
     * {@link Resource Resource authority} of the annotated method. The {@link Resource} provides the URI format to
     * be populated during request enrichment.
     *
     * @return {@link Resource The resource authority}
     * @see RequestEnrichmentAspect
     */
    Resource authority();

    /**
     * HTTP method for the request.
     *
     * @return {@link Http HTTP method}
     */
    Http type();
}
