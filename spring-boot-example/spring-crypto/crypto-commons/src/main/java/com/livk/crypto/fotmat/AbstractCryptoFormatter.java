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

package com.livk.crypto.fotmat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * The type Abstract crypto formatter.
 *
 * @param <T> the type parameter
 */

public abstract class AbstractCryptoFormatter<T> implements CryptoFormatter<T> {

	protected static final Logger LOG = LoggerFactory.getLogger(CryptoFormatter.class);

	@Override
	public final String format(T value) {
		try {
			String convert = convert(value);
			byte[] encrypt = encrypt(convert.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(encrypt);
		}
		catch (Exception e) {
			LOG.error("format error:{}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public final T parse(String text) {
		try {
			byte[] decode = Base64.getDecoder().decode(text);
			byte[] decrypt = decrypt(decode);
			String result = new String(decrypt, StandardCharsets.UTF_8);
			return convert(result);
		}
		catch (Exception e) {
			LOG.error("parse error:{}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * Decrypt byte [ ].
	 * @param bytes the bytes
	 * @return the byte [ ]
	 */
	protected abstract byte[] decrypt(byte[] bytes) throws Exception;

	/**
	 * Encrypt byte [ ].
	 * @param bytes the bytes
	 * @return the byte [ ]
	 */
	protected abstract byte[] encrypt(byte[] bytes) throws Exception;

	/**
	 * Convert t.
	 * @param decryptValue the decrypt value
	 * @return the t
	 */
	protected abstract T convert(String decryptValue) throws Exception;

	/**
	 * Convert string.
	 * @param encryptValue the encrypt value
	 * @return the string
	 */
	protected abstract String convert(T encryptValue) throws Exception;

}
