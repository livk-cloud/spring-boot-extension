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

package com.livk.autoconfigure.useragent;

import com.livk.autoconfigure.useragent.domain.UserAgent;
import com.livk.commons.spring.context.SpringContextHolder;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;

/**
 * The type User agent helper.
 *
 * @author livk
 */
public class UserAgentHelper {

	private static final ConversionService conversionService;

	static {
		conversionService = SpringContextHolder.getBean(ConversionService.class);
	}

	/**
	 * Convert user agent.
	 *
	 * @param headers the headers
	 * @return the user agent
	 */
	public static UserAgent convert(HttpHeaders headers) {
		return conversionService.convert(headers, UserAgent.class);
	}
}
