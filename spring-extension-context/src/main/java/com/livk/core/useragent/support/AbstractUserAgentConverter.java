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

package com.livk.core.useragent.support;

import com.livk.core.useragent.domain.UserAgent;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;

/**
 * The type Abstract user agent converter.
 *
 * @param <T> the type parameter
 * @author livk
 */
public abstract class AbstractUserAgentConverter<T> implements UserAgentConverter {

	@Override
	public UserAgent convert(@NonNull HttpHeaders headers) {
		String useragent = headers.getFirst(HttpHeaders.USER_AGENT);
		UserAgent.UserAgentBuilder builder = UserAgent.builder(useragent);
		T t = create(headers);
		return builder.browser(browser(t))
			.browserType(browserType(t))
			.browserVersion(browserVersion(t))
			.os(os(t))
			.osVersion(osVersion(t))
			.deviceType(deviceType(t))
			.deviceName(deviceName(t))
			.deviceBrand(deviceBrand(t))
			.build();
	}

	/**
	 * Create t.
	 * @param headers the headers
	 * @return the t
	 */
	protected abstract T create(HttpHeaders headers);

	/**
	 * Browser string.
	 * @param t the t
	 * @return the string
	 */
	protected abstract String browser(T t);

	/**
	 * Browser type string.
	 * @param t the t
	 * @return the string
	 */
	protected abstract String browserType(T t);

	/**
	 * Browser version string.
	 * @param t the t
	 * @return the string
	 */
	protected abstract String browserVersion(T t);

	/**
	 * Os string.
	 * @param t the t
	 * @return the string
	 */
	protected abstract String os(T t);

	/**
	 * Os version string.
	 * @param t the t
	 * @return the string
	 */
	protected abstract String osVersion(T t);

	/**
	 * Device type string.
	 * @param t the t
	 * @return the string
	 */
	protected abstract String deviceType(T t);

	/**
	 * Device name string.
	 * @param t the t
	 * @return the string
	 */
	protected abstract String deviceName(T t);

	/**
	 * Device brand string.
	 * @param t the t
	 * @return the string
	 */
	protected abstract String deviceBrand(T t);

}
