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

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class AbstractUserAgentConverterTests {

	final StubConverter converter = new StubConverter();

	@Test
	void convertReturnsUserAgent() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.USER_AGENT, "TestAgent/1.0");
		UserAgent ua = converter.convert(headers);
		assertThat(ua).isNotNull();
		assertThat(ua.userAgentStr()).isEqualTo("TestAgent/1.0");
		assertThat(ua.browser()).isEqualTo("TestBrowser");
	}

	@Test
	void convertCachesResultForSameUserAgent() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.USER_AGENT, "TestAgent/1.0");
		UserAgent first = converter.convert(headers);
		UserAgent second = converter.convert(headers);
		assertThat(first).isSameAs(second);
		assertThat(converter.createCount).isEqualTo(1);
	}

	@Test
	void convertWithNullUserAgentHeaderUsesEmptyString() {
		HttpHeaders headers = new HttpHeaders();
		UserAgent ua = converter.convert(headers);
		assertThat(ua.userAgentStr()).isEmpty();
	}

	@Test
	void convertWithDifferentUserAgentsReturnsDifferentResults() {
		HttpHeaders h1 = new HttpHeaders();
		h1.set(HttpHeaders.USER_AGENT, "Agent1");
		HttpHeaders h2 = new HttpHeaders();
		h2.set(HttpHeaders.USER_AGENT, "Agent2");

		UserAgent ua1 = converter.convert(h1);
		UserAgent ua2 = converter.convert(h2);
		assertThat(ua1).isNotSameAs(ua2);
		assertThat(converter.createCount).isEqualTo(2);
	}

	static class StubConverter extends AbstractUserAgentConverter<String> {

		int createCount = 0;

		@Override
		protected String create(String useragent) {
			createCount++;
			return useragent;
		}

		@Override
		protected String browser(String s) {
			return "TestBrowser";
		}

		@Override
		protected String browserType(String s) {
			return "Browser";
		}

		@Override
		protected String browserVersion(String s) {
			return "1.0";
		}

		@Override
		protected String os(String s) {
			return "TestOS";
		}

		@Override
		protected String osVersion(String s) {
			return "10";
		}

		@Override
		protected String deviceType(String s) {
			return "Desktop";
		}

		@Override
		protected String deviceName(String s) {
			return "PC";
		}

		@Override
		protected String deviceBrand(String s) {
			return "Unknown";
		}

	}

}
