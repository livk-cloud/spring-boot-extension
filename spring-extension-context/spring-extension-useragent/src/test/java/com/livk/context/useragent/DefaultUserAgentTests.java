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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class DefaultUserAgentTests {

	@Test
	void builderSetsAllFields() {
		UserAgent ua = UserAgent.builder("Mozilla/5.0")
			.browser("Chrome")
			.browserType("Browser")
			.browserVersion("120.0")
			.os("Windows")
			.osVersion("10")
			.deviceType("Desktop")
			.deviceName("PC")
			.deviceBrand("Unknown")
			.build();

		assertThat(ua.userAgentStr()).isEqualTo("Mozilla/5.0");
		assertThat(ua.browser()).isEqualTo("Chrome");
		assertThat(ua.browserType()).isEqualTo("Browser");
		assertThat(ua.browserVersion()).isEqualTo("120.0");
		assertThat(ua.os()).isEqualTo("Windows");
		assertThat(ua.osVersion()).isEqualTo("10");
		assertThat(ua.deviceType()).isEqualTo("Desktop");
		assertThat(ua.deviceName()).isEqualTo("PC");
		assertThat(ua.deviceBrand()).isEqualTo("Unknown");
	}

	@Test
	void builderWithDefaultsLeavesFieldsNull() {
		UserAgent ua = UserAgent.builder("Mozilla/5.0").build();
		assertThat(ua.userAgentStr()).isEqualTo("Mozilla/5.0");
		assertThat(ua.browser()).isNull();
		assertThat(ua.os()).isNull();
	}

	@Test
	void builderReturnsUserAgentInstance() {
		UserAgent ua = UserAgent.builder("test").build();
		assertThat(ua).isInstanceOf(DefaultUserAgent.class);
	}

}
