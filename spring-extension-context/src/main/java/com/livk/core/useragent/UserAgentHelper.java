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

package com.livk.core.useragent;

import com.livk.core.useragent.domain.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;

/**
 * The type User agent helper.
 *
 * @author livk
 */
@RequiredArgsConstructor
public class UserAgentHelper {

	private volatile ConversionService conversionService;

	private final ApplicationContext applicationContext;

	/**
	 * Convert user agent.
	 * @param headers the headers
	 * @return the user agent
	 */
	public UserAgent convert(HttpHeaders headers) {
		if (conversionService == null) {
			synchronized (this) {
				if (conversionService == null) {
					conversionService = applicationContext.getBean(ConversionService.class);
				}
			}
		}
		return conversionService.convert(headers, UserAgent.class);
	}

}
