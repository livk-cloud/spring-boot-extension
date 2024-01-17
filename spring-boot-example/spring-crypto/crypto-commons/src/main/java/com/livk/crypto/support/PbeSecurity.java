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

package com.livk.crypto.support;

import com.livk.crypto.CryptoType;
import com.livk.crypto.fotmat.AbstractCryptoFormatter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.GeneralSecurityException;

/**
 * @author livk
 */
public class PbeSecurity extends AbstractCryptoFormatter<Long> {

	private static final String CIPHER_NAME = "PBEwithMD5AndDES";

	private final SecretKey secretKey;

	private final PBEParameterSpec pbeParameterSpec;

	public PbeSecurity(String salt, String password, int iterationCount) throws GeneralSecurityException {
		this(salt.getBytes(), password.toCharArray(), iterationCount);
	}

	public PbeSecurity(byte[] salt, char[] password, int iterationCount) throws GeneralSecurityException {
		PBEKeySpec keySpec = new PBEKeySpec(password);
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(CIPHER_NAME);
		secretKey = secretKeyFactory.generateSecret(keySpec);
		pbeParameterSpec = new PBEParameterSpec(salt, iterationCount);
	}

	@Override
	protected byte[] decrypt(byte[] decode) throws Exception {
		Cipher cipher = Cipher.getInstance(CIPHER_NAME);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParameterSpec);
		return cipher.doFinal(decode);
	}

	@Override
	protected byte[] encrypt(byte[] encode) throws Exception {
		Cipher cipher = Cipher.getInstance(CIPHER_NAME);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParameterSpec);
		return cipher.doFinal(encode);
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
