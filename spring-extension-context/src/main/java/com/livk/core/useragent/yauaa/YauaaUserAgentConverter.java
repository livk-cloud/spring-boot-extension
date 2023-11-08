/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.core.useragent.yauaa;

import com.livk.core.useragent.support.AbstractUserAgentConverter;
import lombok.RequiredArgsConstructor;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.http.HttpHeaders;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class YauaaUserAgentConverter extends AbstractUserAgentConverter<UserAgent> {

	private final UserAgentAnalyzer userAgentAnalyzer;

	@Override
	protected UserAgent create(HttpHeaders headers) {
		Map<String, String> headersConcat = headers.entrySet()
			.stream()
			.collect(Collectors.toMap(Map.Entry::getKey,
				entry -> String.join(",", entry.getValue())));
		return userAgentAnalyzer.parse(headersConcat);
	}

	@Override
	protected String browser(UserAgent userAgent) {
		return userAgent.getValue(UserAgent.AGENT_NAME);
	}

	@Override
	protected String browserType(UserAgent userAgent) {
		return userAgent.getValue(UserAgent.AGENT_CLASS);
	}

	@Override
	protected String browserVersion(UserAgent userAgent) {
		return userAgent.getValue(UserAgent.AGENT_VERSION);
	}

	@Override
	protected String os(UserAgent userAgent) {
		return userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME);
	}

	@Override
	protected String osVersion(UserAgent userAgent) {
		return userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME_VERSION);
	}

	@Override
	protected String deviceType(UserAgent userAgent) {
		return userAgent.getValue(UserAgent.DEVICE_CLASS);
	}

	@Override
	protected String deviceName(UserAgent userAgent) {
		return userAgent.getValue(UserAgent.DEVICE_NAME);
	}

	@Override
	protected String deviceBrand(UserAgent userAgent) {
		return userAgent.getValue(UserAgent.DEVICE_BRAND);
	}
}
