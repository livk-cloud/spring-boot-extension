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

package com.livk.context.lock.annotation;

import com.livk.context.lock.LockType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for declarative distributed lock control on methods.
 * <p>
 * Methods annotated with this annotation will automatically attempt to acquire a
 * distributed lock before execution and release it after completion. If lock acquisition
 * fails, a {@link com.livk.context.lock.exception.LockException} is thrown.
 * <p>
 * The {@link #key()} attribute supports SpEL expressions for dynamic lock key generation.
 * <p>
 * Usage examples: <pre>{@code
 * &#64;DistLock(key = "'order:' + #orderId", type = LockType.LOCK, waitTime = 5)
 * public void processOrder(String orderId) {
 *     // business logic protected by distributed lock
 * }
 *
 * &#64;DistLock(key = "'stock:' + #productId", leaseTime = 30, async = true)
 * public void deductStock(String productId, int quantity) {
 *     // async lock mode
 * }
 * }</pre>
 *
 * @author livk
 * @see com.livk.context.lock.intercept.DistributedLockInterceptor
 * @see LockType
 * @see com.livk.context.lock.DistLockFactory
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistLock {

	/**
	 * The unique lock key, supports SpEL expressions.
	 * <p>
	 * Method parameters can be referenced via SpEL, e.g.
	 * {@code "'prefix:' + #paramName"}.
	 * @return the lock key expression
	 */
	String key();

	/**
	 * The lock type, defaults to {@link LockType#LOCK} (reentrant lock).
	 * @return the lock type
	 * @see LockType
	 */
	LockType type() default LockType.LOCK;

	/**
	 * The lease time in seconds after which the lock is auto-released.
	 * <p>
	 * Default is {@code -1}, meaning the lock never expires until explicitly released.
	 * @return the lease time in seconds, {@code -1} for no expiration
	 */
	long leaseTime() default -1;

	/**
	 * The maximum wait time in seconds for lock acquisition.
	 * <p>
	 * If the lock cannot be acquired within the specified time, a
	 * {@link com.livk.context.lock.exception.LockException} is thrown. Default is 3
	 * seconds.
	 * @return the wait time in seconds
	 */
	long waitTime() default 3;

	/**
	 * Whether to use async lock mode.
	 * <p>
	 * When enabled, the lock operation executes in a non-blocking manner. Note that not
	 * all lock implementations support async mode; unsupported implementations throw
	 * {@link com.livk.context.lock.exception.UnSupportLockException}. Default is
	 * {@code false}.
	 * @return whether async mode is enabled
	 */
	boolean async() default false;

}
