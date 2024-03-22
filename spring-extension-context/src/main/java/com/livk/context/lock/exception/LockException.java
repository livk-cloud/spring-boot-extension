/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.context.lock.exception;

/**
 * <p>
 * LockException
 * </p>
 *
 * @author livk
 */
public class LockException extends RuntimeException {

	/**
	 * Instantiates a new Lock exception.
	 */
	public LockException() {
		super();
	}

	/**
	 * Instantiates a new Lock exception.
	 * @param message the message
	 */
	public LockException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new Lock exception.
	 * @param message the message
	 * @param cause the cause
	 */
	public LockException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new Lock exception.
	 * @param cause the cause
	 */
	public LockException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new Lock exception.
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	protected LockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
