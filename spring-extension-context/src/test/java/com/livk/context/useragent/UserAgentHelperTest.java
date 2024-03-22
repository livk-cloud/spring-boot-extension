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

package com.livk.context.useragent;

import com.livk.context.useragent.domain.UserAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * <p>
 * UserAgentHelperTest
 * </p>
 *
 * @author livk
 */
class UserAgentHelperTest {

	ApplicationContext applicationContext;

	HttpHeaders headers = new HttpHeaders();

	String userAgentStr = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

	@BeforeEach
	public void init() {
		headers.add(HttpHeaders.USER_AGENT, userAgentStr);
	}

	@Test
	void convertBrowscap() {

		applicationContext = new AnnotationConfigApplicationContext(BrowscapConfig.class,
				ConversionServiceConfig.class);

		UserAgentHelper helper = new UserAgentHelper(applicationContext);
		UserAgent userAgent = helper.convert(headers);
		assertNotNull(userAgent);
		assertEquals(userAgentStr, userAgent.userAgentStr());
		assertEquals("Chrome", userAgent.browser());
		assertEquals("Browser", userAgent.browserType());
		assertEquals("120", userAgent.browserVersion());
		assertEquals("Win10", userAgent.os());
		assertEquals("10.0", userAgent.osVersion());
		assertEquals("Desktop", userAgent.deviceType());
		assertEquals("Windows Desktop", userAgent.deviceName());
		assertNull(userAgent.deviceBrand());
	}

	@Test
	void convertYauaa() {

		applicationContext = new AnnotationConfigApplicationContext(YauaaConfig.class, ConversionServiceConfig.class);

		UserAgentHelper helper = new UserAgentHelper(applicationContext);

		UserAgent userAgent = helper.convert(headers);
		assertNotNull(userAgent);
		assertEquals(userAgentStr, userAgent.userAgentStr());
		assertEquals("Chrome", userAgent.browser());
		assertEquals("Browser", userAgent.browserType());
		assertEquals("120", userAgent.browserVersion());
		assertEquals("Windows NT", userAgent.os());
		assertEquals("Windows NT ??", userAgent.osVersion());
		assertEquals("Desktop", userAgent.deviceType());
		assertEquals("Desktop", userAgent.deviceName());
		assertNull(userAgent.deviceBrand());
	}

}
