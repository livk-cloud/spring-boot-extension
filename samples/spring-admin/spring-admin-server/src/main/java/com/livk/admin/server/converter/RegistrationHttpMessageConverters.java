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

package com.livk.admin.server.converter;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.utils.jackson.AdminServerModule;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

/**
 * @author livk
 */
@Component
public class RegistrationHttpMessageConverters extends HttpMessageConverters {

	public RegistrationHttpMessageConverters(AdminServerProperties adminServerProperties) {
		super(jacksonHttpMessageConverter(adminServerProperties));
	}

	private static HttpMessageConverter<?> jacksonHttpMessageConverter(AdminServerProperties adminServerProperties) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.getObjectMapper()
			.registerModule(new AdminServerModule(adminServerProperties.getMetadataKeysToSanitize()));
		return converter;
	}

}
