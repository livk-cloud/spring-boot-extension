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

package com.livk.commons.web;

import com.livk.commons.jackson.JsonMapperUtils;
import com.livk.commons.util.WebUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

/**
 * <p>
 * RequestWrapperTest
 * </p>
 *
 * @author livk
 */
class RequestWrapperTest {

	RequestWrapper wrapper;

	@BeforeEach
	void init() {
		wrapper = new RequestWrapper(new MockHttpServletRequest());
	}

	@Test
	void body() throws IOException {
		Map<String, String> map = Map.of("root", "root");
		wrapper.body(JsonMapperUtils.writeValueAsBytes(map));

		assertArrayEquals(JsonMapperUtils.writeValueAsBytes(map), wrapper.getContentAsByteArray());
		assertEquals(JsonMapperUtils.writeValueAsString(map), wrapper.getContentAsString());
		assertEquals(MediaType.APPLICATION_JSON_VALUE, wrapper.getContentType());
	}

	@Test
	void addHeader() {
		wrapper.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		wrapper.addHeader(HttpHeaders.USER_AGENT, "test");
		wrapper.addHeader(HttpHeaders.AGE, "20");

		HttpHeaders headers = WebUtils.headers(wrapper);
		assertEquals(MediaType.APPLICATION_JSON_VALUE, headers.getFirst(HttpHeaders.CONTENT_TYPE));
		assertEquals("test", headers.getFirst(HttpHeaders.USER_AGENT));
		assertEquals("20", headers.getFirst(HttpHeaders.AGE));
		assertEquals(3, headers.size());
	}

	@Test
	void addParameter() {
		wrapper.addParameter("username", new String[] { "livk", "root", "admin" });
		wrapper.addParameter("password", "123456");

		HttpParameters parameters = WebUtils.params(wrapper);
		assertEquals(List.of("livk", "root", "admin"), parameters.get("username"));
		assertLinesMatch(List.of("123456"), parameters.get("password"));
		assertEquals(2, parameters.size());
	}

}
