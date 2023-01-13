package com.livk.autoconfigure.http.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.web.service.annotation.HttpExchange;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Provider
 * </p>
 *
 * @author livk
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@HttpExchange
public @interface Provider {

    /**
     * Value string.
     *
     * @return the string
     */
    @AliasFor(annotation = Component.class, attribute = "value")
    String value() default "";

    /**
     * Url string.
     *
     * @return the string
     */
    @AliasFor(annotation = HttpExchange.class, attribute = "url")
    String url() default "";

    /**
     * Method string.
     *
     * @return the string
     */
    @AliasFor(annotation = HttpExchange.class, attribute = "method")
    String method() default "";

    /**
     * Content type string.
     *
     * @return the string
     */
    @AliasFor(annotation = HttpExchange.class, attribute = "contentType")
    String contentType() default "";

    /**
     * Accept string [ ].
     *
     * @return the string [ ]
     */
    @AliasFor(annotation = HttpExchange.class, attribute = "accept")
    String[] accept() default {};
}
