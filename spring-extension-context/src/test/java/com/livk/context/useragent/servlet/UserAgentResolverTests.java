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
import com.livk.context.useragent.UserAgentConverter;
import com.livk.context.useragent.UserAgentHelper;
import com.livk.context.useragent.annotation.UserAgentInfo;
import com.livk.context.useragent.yauaa.YauaaUserAgentConverter;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@SpringJUnitConfig(UserAgentResolverTests.MvcConfig.class)
class UserAgentResolverTests {

	MockHttpServletRequest request;

	MockHttpServletResponse response;

	@BeforeEach
	void init() {
		request = new MockHttpServletRequest();
		request.addHeader(HttpHeaders.USER_AGENT,
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
		response = new MockHttpServletResponse();
	}

	@Autowired
	UserAgentResolverTests(UserAgentHelper helper) {
		this.resolver = new UserAgentResolver(helper);
	}

	final HandlerMethodArgumentResolver resolver;

	@Test
	void supportsParameter() throws Exception {
		Method method = this.getClass().getDeclaredMethod("test", UserAgent.class);
		MethodParameter parameter = MethodParameter.forExecutable(method, 0);

		assertThat(resolver.supportsParameter(parameter)).isTrue();
	}

	@Test
	void resolveArgument() throws Exception {
		assertThat(UserAgentContextHolder.getUserAgentContext()).isNull();

		Method method = this.getClass().getDeclaredMethod("test", UserAgent.class);
		MethodParameter parameter = MethodParameter.forExecutable(method, 0);

		ServletWebRequest webRequest = new ServletWebRequest(request, response);
		Object result = resolver.resolveArgument(parameter, null, webRequest, null);

		assertThat(result).isInstanceOf(UserAgent.class).isEqualTo(UserAgentContextHolder.getUserAgentContext());

		UserAgent userAgent = (UserAgent) result;

		assertThat(userAgent).isNotNull();
		assertThat(userAgent.userAgentStr()).isEqualTo(request.getHeader(HttpHeaders.USER_AGENT));
		assertThat(userAgent.browser()).isEqualTo("Chrome");
		assertThat(userAgent.browserType()).isEqualTo("Browser");
		assertThat(userAgent.browserVersion()).isEqualTo("120");
		assertThat(userAgent.os()).isEqualTo("Windows NT");
		assertThat(userAgent.osVersion()).isEqualTo("Windows NT ??");
		assertThat(userAgent.deviceType()).isEqualTo("Desktop");
		assertThat(userAgent.deviceName()).isEqualTo("Desktop");
		assertThat(userAgent.deviceBrand()).isEqualTo("Unknown");

		UserAgentContextHolder.cleanUserAgentContext();
	}

	@SuppressWarnings("unused")
	void test(@UserAgentInfo UserAgent agent) {
	}

	@TestConfiguration
	static class MvcConfig {

		@Bean
		public UserAgentConverter yauaaUserAgentConverter() {
			UserAgentAnalyzer analyzer = UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(10).build();
			return new YauaaUserAgentConverter(analyzer);
		}

		@Bean
		public UserAgentHelper userAgentHelper() {
			return new UserAgentHelper();
		}

	}

}
