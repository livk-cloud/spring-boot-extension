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

package com.livk.sso.commons;

import com.livk.commons.io.ResourceUtils;
import com.livk.commons.util.ObjectUtils;
import com.livk.sso.commons.util.RSAUtils;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 * @author livk
 */
@ConfigurationProperties(RsaKeyProperties.PREFIX)
public class RsaKeyProperties {

	public static final String PREFIX = "rsa.key.jks";

	private static final String DEFAULT_LOCATION = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/jwt.jks";

	private final RSAKey rsaKey;

	public RsaKeyProperties(@Name("location") String location, @Name("password") String password,
			@Name("alias") String alias) {
		if (ObjectUtils.isEmpty(location)) {
			location = DEFAULT_LOCATION;
		}
		Resource jksResource = ResourceUtils.getResource(location);
		if (!jksResource.exists()) {
			try {
				Resource[] resources = ResourceUtils.getResources(location);
				if (resources != null) {
					jksResource = resources[0];
				}
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		rsaKey = RSAUtils.rsaKey(jksResource, password, alias);
	}

	public RSAKey rsaKey() {
		return rsaKey;
	}

}
