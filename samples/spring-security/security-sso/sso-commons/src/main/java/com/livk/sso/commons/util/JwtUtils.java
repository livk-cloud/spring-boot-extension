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

package com.livk.sso.commons.util;

import com.livk.commons.SpringContextHolder;
import com.livk.commons.jackson.JsonMapperUtils;
import com.livk.sso.commons.RsaKeyProperties;
import com.livk.sso.commons.entity.Payload;
import com.livk.sso.commons.entity.User;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * @author livk
 */
@UtilityClass
public class JwtUtils {

	private static final RSAKey RSA_KEY;

	static {
		RSA_KEY = SpringContextHolder.getBean(RsaKeyProperties.class).rsaKey();
	}

	@SneakyThrows
	public String generateToken(User userInfo) {
		JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build();
		Payload payload = new Payload(UUID.randomUUID().toString(), userInfo);
		JWSObject jwsObject = new JWSObject(jwsHeader,
				new com.nimbusds.jose.Payload(JsonMapperUtils.writeValueAsString(payload)));
		RSASSASigner signer = new RSASSASigner(RSA_KEY);
		jwsObject.sign(signer);
		return jwsObject.serialize();
	}

	@SneakyThrows
	public Payload parse(String token) {
		JWSObject jwsObject = JWSObject.parse(token);
		RSASSAVerifier verifier = new RSASSAVerifier(RSA_KEY);
		if (jwsObject.verify(verifier)) {
			String json = jwsObject.getPayload().toString();
			return JsonMapperUtils.readValue(json, Payload.class);
		}
		throw new RuntimeException("500");
	}

}
