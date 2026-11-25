/*
 * Copyright 2021-present the original author or authors.
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
 */

package com.livk.context.sequence.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fills a missing distributed id into a method return value or parameter.
 * <p>
 * {@link #value()} is the business name and supports SpEL. A missing id is {@code null}
 * or a number less than or equal to {@code 0}. Existing positive ids are left unchanged.
 * <p>
 * Leave {@link #param()} empty and {@link #paramIndex()} at {@code -1} to fill a
 * {@code long} or {@code Long} return value. Prefer {@link #paramIndex()} when parameter
 * names are unavailable; {@link #param()} requires the {@code -parameters} compiler flag.
 * Usage examples: <pre>{@code
 * &#64;Seq("order")
 * public long nextId() {
 *     return 0;
 * }
 *
 * &#64;Seq(value = "order", paramIndex = 0)
 * public Order create(Long id) {
 *     return new Order(id);
 * }
 * }</pre>
 *
 * @author livk
 * @see com.livk.context.sequence.interceptor.SequenceInterceptor
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Seq {

	/**
	 * Business name, supports SpEL expressions.
	 * @return the business name expression
	 */
	String value();

	/**
	 * Parameter name to fill. Empty fills the return value instead, unless
	 * {@link #paramIndex()} is set.
	 * @return the parameter name
	 */
	String param() default "";

	/**
	 * Zero-based parameter index to fill. {@code -1} means unused. Takes effect when
	 * {@link #param()} is empty.
	 * @return the parameter index
	 */
	int paramIndex() default -1;

}
