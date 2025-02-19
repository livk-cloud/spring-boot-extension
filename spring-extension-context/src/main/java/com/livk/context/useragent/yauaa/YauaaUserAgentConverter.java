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

package com.livk.context.useragent.yauaa;

import com.livk.context.useragent.AbstractUserAgentConverter;
import lombok.RequiredArgsConstructor;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.http.HttpHeaders;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class YauaaUserAgentConverter extends AbstractUserAgentConverter<UserAgent> {

	private final UserAgentAnalyzer userAgentAnalyzer;

	@Override
	protected UserAgent create(String useragent) {
		return userAgentAnalyzer.parse(useragent);
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
