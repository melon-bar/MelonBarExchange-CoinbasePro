package com.coinbase.exchange.annotation;

import com.coinbase.exchange.model.request.RequestBodyFieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BodyField {

    String key();

    RequestBodyFieldType jsonType() default RequestBodyFieldType.STRING;

    boolean required() default false;
}
