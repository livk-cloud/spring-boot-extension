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

import com.livk.commons.util.ObjectUtils;
import com.livk.crypto.CryptoType;
import com.livk.crypto.exception.MetadataIllegalException;
import com.livk.crypto.parse.AbstractCryptoFormatter;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.util.Map;

/**
 * @author livk
 */
@Slf4j
public class PbeSecurity extends AbstractCryptoFormatter<Long> {

    private static final String CIPHER_NAME = "PBEwithMD5AndDES";

    private final SecretKey secretKey;

    private final PBEParameterSpec pbeParameterSpec;

    public PbeSecurity(Map<String, String> metadata) {
        String salt = metadata.get("salt");
        if (ObjectUtils.isEmpty(salt)) {
            throw new MetadataIllegalException("缺少salt的配置!", "请添加 'spring.crypto.metadata.salt' ");
        }
        String password = metadata.get("password");
        if (ObjectUtils.isEmpty(password)) {
            throw new MetadataIllegalException("缺少password的配置!", "请添加 'spring.crypto.metadata.password' ");
        }
        try {
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(CIPHER_NAME);
            secretKey = secretKeyFactory.generateSecret(keySpec);
            pbeParameterSpec = new PBEParameterSpec(salt.getBytes(), 1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected byte[] decrypt(byte[] decode) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParameterSpec);
            return cipher.doFinal(decode);
        } catch (Exception e) {
            log.error("decrypt error:{}", e.getMessage());
            return EMPTY;
        }
    }

    @Override
    protected byte[] encrypt(byte[] encode) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParameterSpec);
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
        return CryptoType.PBE;
    }
}
