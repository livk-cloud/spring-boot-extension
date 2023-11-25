/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.autoconfigure.http.annotation;

import com.livk.autoconfigure.http.adapter.AdapterType;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.web.service.annotation.HttpExchange;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link HttpExchange}的变体，将会被Spring加入Context
 * <p>
 * 与{@link HttpExchange}共同使用会出现不可预知的错误
 *
 * @author livk
 * @see HttpExchange
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Component
@HttpExchange
public @interface HttpProvider {

	/**
	 * 指定适配器模式
	 * @return AdapterType
	 * @see AdapterType
	 */
	AdapterType type() default AdapterType.AUTO;

	/**
	 * The value may indicate a suggestion for a logical component name, to be turned into
	 * a Spring bean in case of an autodetected component.
	 * @return the suggested component name, if any (or empty String otherwise)
	 */
	@AliasFor(annotation = Component.class, attribute = "value")
	String value() default "";

	/**
	 * The URL for the request, either a full URL or a path only that is relative to a URL
	 * declared in a type-level {@code @HttpExchange}, and/or a globally configured base
	 * URL.
	 * <p>
	 * By default, this is empty.
	 * @return http url
	 */
	@AliasFor(annotation = HttpExchange.class, attribute = "url")
	String url() default "";

	/**
	 * The HTTP method to use.
	 * <p>
	 * Supported at the type level as well as at the method level. When used at the type
	 * level, all method-level mappings inherit this value.
	 * <p>
	 * By default, this is empty.
	 * @return http method
	 */
	@AliasFor(annotation = HttpExchange.class, attribute = "method")
	String method() default "";

	/**
	 * The media type for the {@code "Content-Type"} header.
	 * <p>
	 * Supported at the type level as well as at the method level, in which case the
	 * method-level values override type-level values.
	 * <p>
	 * By default, this is empty.
	 * @return http contentType
	 */
	@AliasFor(annotation = HttpExchange.class, attribute = "contentType")
	String contentType() default "";

	/**
	 * The media types for the {@code "Accept"} header.
	 * <p>
	 * Supported at the type level as well as at the method level, in which case the
	 * method-level values override type-level values.
	 * <p>
	 * By default, this is empty.
	 * @return http accept
	 */
	@AliasFor(annotation = HttpExchange.class, attribute = "accept")
	String[] accept() default {};

}
