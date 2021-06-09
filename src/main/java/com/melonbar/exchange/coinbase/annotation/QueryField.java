package com.melonbar.exchange.coinbase.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO
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
