/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
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

package com.livk.context.qrcode.exception;

import lombok.Getter;

/**
 * @author livk
 */
@Getter
public class QrCodeException extends RuntimeException {

	/**
	 * Instantiates a new Qr code exception.
	 * @param message the message
	 */
	public QrCodeException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new Qr code exception.
	 * @param message the message
	 * @param cause the cause
	 */
	public QrCodeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new Qr code exception.
	 * @param cause the cause
	 */
	public QrCodeException(Throwable cause) {
		super(cause);
	}

}
