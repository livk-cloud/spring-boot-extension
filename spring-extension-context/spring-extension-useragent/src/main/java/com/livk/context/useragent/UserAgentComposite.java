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

package com.livk.context.useragent;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * The type User agent composite.
 *
 * @author livk
 */
@RequiredArgsConstructor
public final class UserAgentComposite {

	private final ObjectProvider<UserAgentConverter> provider;

	private volatile List<UserAgentConverter> converters;

	/**
	 * Convert user agent.
	 * @param headers the headers
	 * @return the user agent
	 */
	public UserAgent convert(HttpHeaders headers) {
		for (UserAgentConverter converter : getConverters()) {
			UserAgent userAgent = converter.convert(headers);
			if (userAgent != null) {
				return userAgent;
			}
		}
		throw new IllegalStateException(
				"No UserAgentConverter could convert User-Agent: " + headers.getFirst(HttpHeaders.USER_AGENT));
	}

	private List<UserAgentConverter> getConverters() {
		List<UserAgentConverter> result = this.converters;
		if (result == null) {
			synchronized (this) {
				result = this.converters;
				if (result == null) {
					result = this.provider.orderedStream().toList();
					this.converters = result;
				}
			}
		}
		return result;
	}

}
