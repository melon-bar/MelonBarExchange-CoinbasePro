package com.coinbase.annotation;

import com.coinbase.api.resource.Resource;
import com.coinbase.http.Http;
import com.coinbase.model.request.BaseRequest;

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
    Resource authority();
    Http type();
}
