package com.melonbar.exchange.coinbase.annotation;

import com.melonbar.core.http.request.BaseRequest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a member variable for any extension of {@link BaseRequest} as a field for the jsonified request body.
 * During request body generation, the member's <code>toString</code> implementation is invoked.
 *
 * <p> Unlike {@link RequestField}, during execution-level enrichment, the inheritance hierarchy is traversed in
 * search for all occurrences of this annotation.
 *
 * <p> Currently, nested keys are not supported, since Coinbase Pro API requests (so far) don't require any
 * nested json objects. This is subject to change.
 *
 * <p> TODO: Currently we always invoke the <code>toString</code> implementation, but there is a chance certain
 *      types will need to be non-string values in the json body. E.g. using true instead of "true".
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BodyField {

    /**
     * @return Key for body parameter
     */
    String key();

    /**
     * @return True if field is required for generating request body
     */
    boolean required() default false;
}
