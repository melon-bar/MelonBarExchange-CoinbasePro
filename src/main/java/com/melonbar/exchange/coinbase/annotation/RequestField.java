package com.melonbar.exchange.coinbase.annotation;

import com.melonbar.core.http.request.BaseRequest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a member variable for any extension of {@link BaseRequest} as a URI parameter. During URI formatting,
 * automatically invokes the member's <code>toString</code> implementation.
 *
 * <p> It is important to note that the inheritance hierarchy is not traversed in search of this annotation. In
 * other words, all request fields should be defined within a single implementation.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestField {

    /**
     * @return Index of target parameter for URI formatting
     */
    int index();

    /**
     * @return True if field is required to construct URI, false otherwise
     */
    boolean required() default true;
}
