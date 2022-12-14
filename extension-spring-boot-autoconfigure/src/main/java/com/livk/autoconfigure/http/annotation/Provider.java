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

    @AliasFor(annotation = Component.class, attribute = "value")
    String value() default "";

    @AliasFor(annotation = HttpExchange.class, attribute = "url")
    String url() default "";

    @AliasFor(annotation = HttpExchange.class, attribute = "method")
    String method() default "";

    @AliasFor(annotation = HttpExchange.class, attribute = "contentType")
    String contentType() default "";

    @AliasFor(annotation = HttpExchange.class, attribute = "accept")
    String[] accept() default {};
}
