/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.context.useragent;

import lombok.Setter;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;

/**
 * The type User agent helper.
 *
 * @author livk
 */
@Setter
public class UserAgentHelper implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	/**
	 * Convert user agent.
	 * @param headers the headers
	 * @return the user agent
	 */
	public UserAgent convert(HttpHeaders headers) {
		for (UserAgentConverter converter : applicationContext.getBeanProvider(UserAgentConverter.class)) {
			UserAgent userAgent = converter.convert(headers);
			if (userAgent != null) {
				return userAgent;
			}
		}
		throw new NoSuchBeanDefinitionException(UserAgentConverter.class);
	}

}
