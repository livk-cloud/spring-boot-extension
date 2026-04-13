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

package com.livk.context.useragent.browscap;

import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;
import com.livk.context.useragent.UserAgent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class BrowscapUserAgentConverterTests {

	static BrowscapUserAgentConverter converter;

	@BeforeAll
	static void init() throws IOException, ParseException {
		UserAgentParser parser = new UserAgentService().loadParser();
		converter = new BrowscapUserAgentConverter(parser);
	}

	@Test
	void convertParsesChromeBrowser() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.USER_AGENT,
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
		UserAgent ua = converter.convert(headers);
		assertThat(ua).isNotNull();
		assertThat(ua.browser()).isEqualTo("Chrome");
		assertThat(ua.os()).isEqualTo("Win10");
	}

	@Test
	void convertReturnsBrowserVersion() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.USER_AGENT,
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
		UserAgent ua = converter.convert(headers);
		assertThat(ua.browserVersion()).isNotNull().isNotEmpty();
	}

	@Test
	void convertReturnsDeviceType() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.USER_AGENT,
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
		UserAgent ua = converter.convert(headers);
		assertThat(ua.deviceType()).isNotNull();
	}

}
