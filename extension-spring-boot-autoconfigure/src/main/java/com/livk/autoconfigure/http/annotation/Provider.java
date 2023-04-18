/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
