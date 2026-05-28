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
import com.livk.context.useragent.UserAgentHelper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class UserAgentFilterTests {

	@AfterEach
	void tearDown() {
		UserAgentContextHolder.cleanUserAgentContext();
	}

	@Test
	void doFilterClearsContextWhenChainThrows() throws ServletException, IOException {
		UserAgentHelper helper = mock(UserAgentHelper.class);
		UserAgent userAgent = UserAgent.builder("Mozilla/5.0").browser("Chrome").build();
		given(helper.convert(any(HttpHeaders.class))).willReturn(userAgent);
		UserAgentFilter filter = new UserAgentFilter(helper);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		assertThatThrownBy(() -> filter.doFilter(request, response, (servletRequest, servletResponse) -> {
			assertThat(UserAgentContextHolder.getUserAgentContext()).isSameAs(userAgent);
			throw new ServletException("boom");
		})).isInstanceOf(ServletException.class);

		assertThat(UserAgentContextHolder.getUserAgentContext()).isNull();
	}

}
