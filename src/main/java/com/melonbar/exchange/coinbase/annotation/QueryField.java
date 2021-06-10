package com.melonbar.exchange.coinbase.annotation;

import com.melonbar.exchange.coinbase.enrichment.RequestEnricher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark request class members as query parameters, which will be constructed into a query string by the
 * {@link RequestEnricher} during execution level.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryField {

    /**
     * @return Key for query field
     */
    String key();

    /**
     * @return True if field is required for generating query string
     */
    boolean required() default false;
}
