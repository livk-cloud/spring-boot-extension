package com.livk.commons.jackson.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.livk.commons.jackson.support.NumberJsonSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Number json format.
 *
 * @author livk
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = NumberJsonSerializer.class)
public @interface NumberJsonFormat {

    /**
     * Pattern string.
     *
     * @return the string
     */
    String pattern() default "#0.00";

    /**
     * Simple type support boolean.
     *
     * @return the boolean
     */
    boolean simpleTypeSupport() default true;
}
