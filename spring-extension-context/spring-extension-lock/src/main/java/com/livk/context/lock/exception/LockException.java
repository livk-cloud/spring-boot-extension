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
 * Base exception class for distributed lock operations.
 * <p>
 * Thrown when distributed lock operations (acquire, release, etc.) encounter errors.
 * Common scenarios include:
 * <ul>
 * <li>Lock acquisition timeout or interruption</li>
 * <li>Errors during lock release</li>
 * <li>Communication failures with the underlying distributed component</li>
 * </ul>
 *
 * @author livk
 * @see UnSupportLockException
 */
public class LockException extends RuntimeException {

	/**
	 * Create a new lock exception with no detail message.
	 */
	public LockException() {
		super();
	}

	/**
	 * Create a new lock exception with the specified detail message.
	 * @param message the detail message
	 */
	public LockException(String message) {
		super(message);
	}

	/**
	 * Create a new lock exception with the specified detail message and cause.
	 * @param message the detail message
	 * @param cause the root cause
	 */
	public LockException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a new lock exception with the specified cause.
	 * @param cause the root cause
	 */
	public LockException(Throwable cause) {
		super(cause);
	}

	/**
	 * Create a new lock exception with full configuration.
	 * @param message the detail message
	 * @param cause the root cause
	 * @param enableSuppression whether suppression is enabled
	 * @param writableStackTrace whether the stack trace is writable
	 */
	protected LockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
