/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.commons.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.util.JsonMapperUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

/**
 * <p>
 * WebUtilsTest
 * </p>
 *
 * @author livk
 */
class WebUtilsTest {

	MockHttpServletRequest request;

	MockHttpServletResponse response;

	@BeforeEach
	void init() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@Test
	void request() {
		ServletWebRequest servletWebRequest = new ServletWebRequest(request, response);
		RequestContextHolder.setRequestAttributes(servletWebRequest);
		HttpServletRequest req = WebUtils.request();
		assertEquals(request, req);
	}

	@Test
	void response() {
		ServletWebRequest servletWebRequest = new ServletWebRequest(request, response);
		RequestContextHolder.setRequestAttributes(servletWebRequest);
		HttpServletResponse resp = WebUtils.response();
		assertEquals(response, resp);
	}

	@Test
	void headers() {
		request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		request.addHeader(HttpHeaders.USER_AGENT, "test");

		HttpHeaders headers = WebUtils.headers(request);
		assertEquals(MediaType.APPLICATION_JSON_VALUE, headers.getFirst(HttpHeaders.CONTENT_TYPE));
		assertEquals("test", headers.getFirst(HttpHeaders.USER_AGENT));
		assertEquals(2, headers.size());
	}

	@Test
	void attributes() {
		request.setAttribute("username", "livk");
		request.setAttribute("password", "123456");

		Map<String, Object> attributes = WebUtils.attributes(request);
		assertEquals("livk", attributes.get("username"));
		assertEquals("123456", attributes.get("password"));
		assertEquals(2, attributes.size());
	}

	@Test
	void paramMap() {
		request.addParameter("username", "livk", "root", "admin");
		request.addParameter("password", "123456");

		Map<String, String> paramMap = WebUtils.paramMap(request, ",");
		assertEquals("livk,root,admin", paramMap.get("username"));
		assertEquals("123456", paramMap.get("password"));
		assertEquals(2, paramMap.size());
	}

	@Test
	void params() {
		request.addParameter("username", "livk", "root", "admin");
		request.addParameter("password", "123456");

		MultiValueMap<String, String> params = WebUtils.params(request);
		assertEquals(List.of("livk", "root", "admin"), params.get("username"));
		assertLinesMatch(List.of("123456"), params.get("password"));
		assertEquals(2, params.size());
	}

	@Test
	void realIp() {
		assertEquals("127.0.0.1", WebUtils.realIp(request));
	}

	@Test
	void outJson() {
		Map<String, String> result = Map.of("username", "livk", "password", "123456");
		WebUtils.outJson(response, result);

		JsonNode node = JsonMapperUtils.readTree(response.getContentAsByteArray());
		assertEquals("livk", node.get("username").asText());
		assertEquals("123456", node.get("password").asText());
	}

	@Test
	void out() {
		WebUtils.out(response, "out buffer text", MediaType.TEXT_PLAIN_VALUE);

		String result = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
		assertEquals("out buffer text", result);
	}

}
