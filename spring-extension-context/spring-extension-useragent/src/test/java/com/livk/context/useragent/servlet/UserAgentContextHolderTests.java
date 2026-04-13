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

package com.livk.context.useragent.servlet;

import com.livk.context.useragent.UserAgent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class UserAgentContextHolderTests {

	@AfterEach
	void tearDown() {
		UserAgentContextHolder.cleanUserAgentContext();
	}

	@Test
	void withAndGetUserAgentContext() {
		UserAgent ua = UserAgent.builder("Mozilla/5.0").browser("Chrome").build();
		UserAgentContextHolder.withUserAgentContext(ua);
		assertThat(UserAgentContextHolder.getUserAgentContext()).isSameAs(ua);
	}

	@Test
	void getUserAgentContextReturnsNullWhenNotSet() {
		assertThat(UserAgentContextHolder.getUserAgentContext()).isNull();
	}

	@Test
	void cleanUserAgentContextRemovesValue() {
		UserAgent ua = UserAgent.builder("Mozilla/5.0").build();
		UserAgentContextHolder.withUserAgentContext(ua);
		UserAgentContextHolder.cleanUserAgentContext();
		assertThat(UserAgentContextHolder.getUserAgentContext()).isNull();
	}

}
