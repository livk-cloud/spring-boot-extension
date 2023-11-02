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

package com.livk.core.useragent.browscap;

import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.UserAgentParser;
import com.livk.core.useragent.support.AbstractUserAgentConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class BrowscapUserAgentConverter extends AbstractUserAgentConverter<Capabilities> {

	private final UserAgentParser userAgentParser;

	@Override
	protected Capabilities create(HttpHeaders headers) {
		String userAgent = headers.getFirst(HttpHeaders.USER_AGENT);
		return userAgentParser.parse(userAgent);
	}

	@Override
	protected String browser(Capabilities capabilities) {
		return capabilities.getBrowser();
	}

	@Override
	protected String browserType(Capabilities capabilities) {
		return capabilities.getBrowserType();
	}

	@Override
	protected String browserVersion(Capabilities capabilities) {
		return capabilities.getBrowserMajorVersion();
	}

	@Override
	protected String os(Capabilities capabilities) {
		return capabilities.getPlatform();
	}

	@Override
	protected String osVersion(Capabilities capabilities) {
		return capabilities.getPlatformVersion();
	}

	@Override
	protected String deviceType(Capabilities capabilities) {
		return capabilities.getDeviceType();
	}

	@Override
	protected String deviceName(Capabilities capabilities) {
		return capabilities.getValue(BrowsCapField.DEVICE_NAME);
	}

	@Override
	protected String deviceBrand(Capabilities capabilities) {
		return capabilities.getValue(BrowsCapField.DEVICE_BRAND_NAME);
	}
}
