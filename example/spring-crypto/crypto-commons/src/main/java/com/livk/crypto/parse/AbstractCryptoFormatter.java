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

package com.livk.crypto.parse;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;

/**
 * The type Abstract crypto formatter.
 *
 * @param <T> the type parameter
 */
public abstract class AbstractCryptoFormatter<T> implements CryptoFormatter<T> {

    protected static final byte[] EMPTY = new byte[0];

    @Override
    public T parse(String text, Locale locale) {
        byte[] decode = Base64.getDecoder().decode(text);
        byte[] decrypt = decrypt(decode);
        String result = new String(decrypt, StandardCharsets.UTF_8);
        return convert(result);
    }

    @Override
    public String print(T value, Locale locale) {
        String convert = convert(value);
        byte[] encrypt = encrypt(convert.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypt);
    }

    /**
     * Decrypt byte [ ].
     *
     * @param bytes the bytes
     * @return the byte [ ]
     */
    protected abstract byte[] decrypt(byte[] bytes);

    /**
     * Encrypt byte [ ].
     *
     * @param bytes the bytes
     * @return the byte [ ]
     */
    protected abstract byte[] encrypt(byte[] bytes);

    /**
     * Convert t.
     *
     * @param decryptValue the decrypt value
     * @return the t
     */
    protected abstract T convert(String decryptValue);

    /**
     * Convert string.
     *
     * @param encryptValue the encrypt value
     * @return the string
     */
    protected abstract String convert(T encryptValue);
}
