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

package com.livk.crypto.support;

import com.livk.crypto.CryptoType;
import com.livk.crypto.exception.MetadataIllegalException;
import com.livk.crypto.parse.AbstractCryptoFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Map;

/**
 * @author livk
 */
@Slf4j
public class AesSecurity extends AbstractCryptoFormatter<Long> {
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private final SecretKeySpec spec;

    public AesSecurity(Map<String, String> metadata) {
        String salt = metadata.get("salt");
        if (!StringUtils.hasText(salt)) {
            throw new MetadataIllegalException("缺少salt的配置!", "请添加 'spring.crypto.metadata.salt' ");
        }
        try {
            KeyGenerator kg = KeyGenerator.getInstance(CryptoType.AES.getAlgorithm());
            kg.init(256, new SecureRandom(salt.getBytes()));
            SecretKey secretKey = kg.generateKey();
            spec = new SecretKeySpec(secretKey.getEncoded(), CryptoType.AES.getAlgorithm());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected byte[] decrypt(byte[] decode) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, spec);
            return cipher.doFinal(decode);
        } catch (Exception e) {
            log.error("decrypt error:{}", e.getMessage());
            return EMPTY;
        }
    }

    @Override
    protected byte[] encrypt(byte[] encode) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, spec);
            return cipher.doFinal(encode);
        } catch (Exception e) {
            log.error("encrypt error:{}", e.getMessage());
            return EMPTY;
        }
    }

    @Override
    protected Long convert(String decryptValue) {
        return Long.parseLong(decryptValue);
    }

    @Override
    protected String convert(Long encryptValue) {
        return encryptValue.toString();
    }

    @Override
    public CryptoType type() {
        return CryptoType.AES;
    }
}
