package com.coinbase.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Helpful:
 * 1. Set annotation field default value at RUNTIME:
 *  https://stackoverflow.com/questions/25348572/get-the-name-of-an-annotated-field
 * 2. General tutorial:
 *  http://tutorials.jenkov.com/java-reflection/annotations.html
 *  https://www.baeldung.com/java-annotation-processing-builder#:~:text=3.-,Annotation%20Processing%20API,called%20on%20the%20corresponding%20sources.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Field {
    String name();
    String description() default "No description.";
    Class<?> type() default Object.class;
    boolean required() default true;
}
