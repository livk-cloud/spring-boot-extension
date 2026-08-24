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

package com.livk.context.lock.exception;

/**
 * Exception thrown when a requested lock operation is not supported by the current
 * implementation.
 * <p>
 * A typical scenario is invoking async lock methods on an implementation that does not
 * support asynchronous operations, such as
 * {@link com.livk.context.lock.support.CuratorLockFactory}.
 *
 * @author livk
 * @see LockException
 * @see com.livk.context.lock.support.AbstractLockSupport#tryLockAsync
 * @see com.livk.context.lock.support.AbstractLockSupport#doLockAsync
 */
public class UnSupportLockException extends RuntimeException {

	/**
	 * Create a new unsupported lock operation exception with the specified detail
	 * message.
	 * @param message the detail message describing which operation is unsupported
	 */
	public UnSupportLockException(String message) {
		super(message);
	}

	/**
	 * Create a new unsupported lock operation exception with the specified detail message
	 * and cause.
	 * @param message the detail message
	 * @param cause the root cause
	 */
	public UnSupportLockException(String message, Throwable cause) {
		super(message, cause);
	}

}
