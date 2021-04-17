package com.coinbase.annotation;

import com.coinbase.api.resource.Resource;
import com.coinbase.http.Http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnrichRequest {
    Resource authority();
    Http type();
}
