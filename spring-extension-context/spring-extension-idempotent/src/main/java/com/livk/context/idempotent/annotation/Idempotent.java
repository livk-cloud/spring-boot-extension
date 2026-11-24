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

package com.livk.context.idempotent.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Marks a method as idempotent.
 * <p>
 * The first invocation occupies {@link #key()} for {@link #ttl()}. Later invocations with
 * the same key fail with
 * {@link com.livk.context.idempotent.exception.IdempotentException} until the token
 * expires. When the method throws and {@link #releaseOnError()} is {@code true}, the
 * token is released so the caller can retry.
 * <p>
 * {@link #key()} supports SpEL. Usage example: <pre>{@code
 * &#64;Idempotent(key = "'order:' + #orderId", ttl = 30)
 * public void submit(String orderId) {
 *     // business logic
 * }
 * }</pre>
 *
 * @author livk
 * @see com.livk.context.idempotent.interceptor.IdempotentInterceptor
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {

	/**
	 * Idempotent key, supports SpEL expressions.
	 * @return the key expression
	 */
	String key();

	/**
	 * How long a successful invocation occupies the key.
	 * @return the time-to-live
	 */
	long ttl() default 60;

	/**
	 * Time unit of {@link #ttl()}.
	 * @return the time unit
	 */
	TimeUnit ttlUnit() default TimeUnit.SECONDS;

	/**
	 * Whether to release the token when the method throws, allowing a retry.
	 * @return {@code true} to release on failure
	 */
	boolean releaseOnError() default true;

}
