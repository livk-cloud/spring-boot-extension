/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.useragent.servlet;

import com.livk.context.useragent.UserAgentHelper;
import com.livk.context.useragent.annotation.UserAgentInfo;
import com.livk.context.useragent.domain.UserAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
@SpringJUnitConfig(MvcConfig.class)
class UserAgentResolverTest {

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
	UserAgentResolverTest(UserAgentHelper helper) {
		this.resolver = new UserAgentResolver(helper);
	}

	final HandlerMethodArgumentResolver resolver;

	@Test
	void supportsParameter() throws Exception {
		Method method = this.getClass().getDeclaredMethod("test", UserAgent.class);
		MethodParameter parameter = MethodParameter.forExecutable(method, 0);

		assertTrue(resolver.supportsParameter(parameter));
	}

	@Test
	void resolveArgument() throws Exception {

		assertNull(UserAgentContextHolder.getUserAgentContext());
		Method method = this.getClass().getDeclaredMethod("test", UserAgent.class);
		MethodParameter parameter = MethodParameter.forExecutable(method, 0);

		ServletWebRequest webRequest = new ServletWebRequest(request, response);
		Object result = resolver.resolveArgument(parameter, null, webRequest, null);

		assertInstanceOf(UserAgent.class, result);

		UserAgent userAgent = (UserAgent) result;
		assertNotNull(userAgent);
		assertEquals(request.getHeader(HttpHeaders.USER_AGENT), userAgent.userAgentStr());
		assertEquals("Chrome", userAgent.browser());
		assertEquals("Browser", userAgent.browserType());
		assertEquals("120", userAgent.browserVersion());
		assertEquals("Windows NT", userAgent.os());
		assertEquals("Windows NT ??", userAgent.osVersion());
		assertEquals("Desktop", userAgent.deviceType());
		assertEquals("Desktop", userAgent.deviceName());
		assertNull(userAgent.deviceBrand());

		assertEquals(result, UserAgentContextHolder.getUserAgentContext());

		UserAgentContextHolder.cleanUserAgentContext();
	}

	@SuppressWarnings("unused")
	void test(@UserAgentInfo UserAgent agent) {
	}

}
