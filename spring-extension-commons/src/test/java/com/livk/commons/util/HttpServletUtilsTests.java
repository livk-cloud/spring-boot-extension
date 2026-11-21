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

package com.livk.commons.util;

import com.livk.commons.jackson.JsonMapperUtils;
import com.livk.commons.web.HttpParameters;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import tools.jackson.databind.JsonNode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author livk
 */
class HttpServletUtilsTests {

	MockHttpServletRequest request;

	MockHttpServletResponse response;

	@BeforeEach
	void init() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@AfterEach
	void clearRequestAttributes() {
		RequestContextHolder.resetRequestAttributes();
	}

	@Test
	void request() {
		ServletWebRequest servletWebRequest = new ServletWebRequest(request, response);
		RequestContextHolder.setRequestAttributes(servletWebRequest);

		HttpServletRequest req = HttpServletUtils.request();
		assertThat(req).isSameAs(request);
	}

	@Test
	void requestFromServletRequestAttributes() {
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));
		assertThat(HttpServletUtils.request()).isSameAs(request);
		assertThat(HttpServletUtils.response()).isSameAs(response);
	}

	@Test
	void requestThrowsWhenAttributesUnsupported() {
		RequestContextHolder.setRequestAttributes(mock(RequestAttributes.class));
		assertThatThrownBy(HttpServletUtils::request).isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("request not found");
		assertThatThrownBy(HttpServletUtils::response).isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("response not found");
	}

	@Test
	void response() {
		ServletWebRequest servletWebRequest = new ServletWebRequest(request, response);
		RequestContextHolder.setRequestAttributes(servletWebRequest);

		HttpServletResponse resp = HttpServletUtils.response();
		assertThat(resp).isSameAs(response);
	}

	@Test
	void headers() {
		request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		request.addHeader(HttpHeaders.USER_AGENT, "test");

		HttpHeaders headers = HttpServletUtils.extractHeaders(request);
		assertThat(headers.getFirst(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
		assertThat(headers.getFirst(HttpHeaders.USER_AGENT)).isEqualTo("test");
		assertThat(headers.size()).isEqualTo(2);
	}

	@Test
	void attributes() {
		request.setAttribute("username", "livk");
		request.setAttribute("password", "123456");

		Map<String, Object> attributes = HttpServletUtils.attributes(request);
		assertThat(attributes).containsEntry("username", "livk").containsEntry("password", "123456").hasSize(2);
	}

	@Test
	void params() {
		request.addParameter("username", "livk", "root", "admin");
		request.addParameter("password", "123456");

		HttpParameters parameters = HttpServletUtils.params(request);
		assertThat(parameters.get("username")).containsExactly("livk", "root", "admin");
		assertThat(parameters.get("password")).containsExactly("123456");
		assertThat(parameters).hasSize(2);
	}

	@Test
	void realIp() {
		assertThat(HttpServletUtils.realIp(request)).isEqualTo("127.0.0.1");
	}

	@Test
	void realIpWithXForwardedFor() {
		request.addHeader("X-Forwarded-For", "10.0.0.1, 192.168.1.1");
		assertThat(HttpServletUtils.realIp(request)).isEqualTo("10.0.0.1");
	}

	@Test
	void realIpWithXRealIp() {
		request.addHeader("X-Real-IP", "172.16.0.1");
		assertThat(HttpServletUtils.realIp(request)).isEqualTo("172.16.0.1");
	}

	@Test
	void realIpIgnoresUnknownHeader() {
		request.addHeader("X-Forwarded-For", "unknown");
		assertThat(HttpServletUtils.realIp(request)).isEqualTo("127.0.0.1");
	}

	@Test
	void realIpIgnoresBlankHeader() {
		request.addHeader("X-Forwarded-For", "   ");
		assertThat(HttpServletUtils.realIp(request)).isEqualTo("127.0.0.1");
	}

	@Test
	void toHttpHeaders() {
		request.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpHeaders headers = HttpServletUtils.toHttpHeaders(request);
		assertThat(headers.getAccept()).contains(MediaType.APPLICATION_JSON);
	}

	@Test
	void writerWrapsIoException() throws IOException {
		HttpServletResponse broken = mock(HttpServletResponse.class);
		given(broken.getWriter()).willThrow(new IOException("boom"));
		assertThatThrownBy(() -> HttpServletUtils.writer(broken, "msg", MediaType.TEXT_PLAIN_VALUE))
			.isInstanceOf(ErrorResponseException.class);
	}

	@Test
	void outJson() {
		Map<String, String> result = Map.of("username", "livk", "password", "123456");
		HttpServletUtils.writerJson(response, result);

		JsonNode node = JsonMapperUtils.readTree(response.getContentAsByteArray());
		assertThat(node.get("username").asString()).isEqualTo("livk");
		assertThat(node.get("password").asString()).isEqualTo("123456");
	}

	@Test
	void out() {
		HttpServletUtils.writer(response, "out buffer text", MediaType.TEXT_PLAIN_VALUE);

		String result = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
		assertThat(result).isEqualTo("out buffer text");
	}

}
