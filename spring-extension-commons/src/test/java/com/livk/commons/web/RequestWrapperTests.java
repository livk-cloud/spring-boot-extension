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

package com.livk.commons.web;

import com.livk.commons.jackson.JsonMapperUtils;
import com.livk.commons.util.HttpServletUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class RequestWrapperTests {

	static final Map<String, String> BODY_MAP = Map.of("root", "root");

	static final byte[] BODY_BYTES = JsonMapperUtils.writeValueAsBytes(BODY_MAP);

	RequestWrapper wrapper;

	@BeforeEach
	void init() {
		wrapper = new RequestWrapper(new MockHttpServletRequest());
	}

	// --- body ---

	@Test
	void bodySetsByteContentWithDefaultContentType() throws IOException {
		wrapper.body(BODY_BYTES);
		assertThat(wrapper.getContentAsByteArray()).isEqualTo(BODY_BYTES);
		assertThat(wrapper.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
	}

	@Test
	void bodyWithCustomContentType() throws IOException {
		wrapper.body(BODY_BYTES, MediaType.TEXT_PLAIN_VALUE);
		assertThat(wrapper.getContentAsByteArray()).isEqualTo(BODY_BYTES);
		assertThat(wrapper.getContentType()).isEqualTo(MediaType.TEXT_PLAIN_VALUE);
	}

	@Test
	void getContentAsStringReturnsBodyAsString() throws IOException {
		wrapper.body(BODY_BYTES);
		assertThat(wrapper.getContentAsString()).isEqualTo(JsonMapperUtils.writeValueAsString(BODY_MAP));
	}

	@Test
	void getInputStreamReturnsBodyContent() throws IOException {
		wrapper.body(BODY_BYTES);
		byte[] read = wrapper.getInputStream().readAllBytes();
		assertThat(read).isEqualTo(BODY_BYTES);
	}

	@Test
	void getReaderReturnsBodyContent() throws IOException {
		wrapper.body(BODY_BYTES);
		BufferedReader reader = wrapper.getReader();
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		assertThat(sb.toString()).isEqualTo(JsonMapperUtils.writeValueAsString(BODY_MAP));
	}

	@Test
	void getContentLengthReflectsBodySize() throws IOException {
		wrapper.body(BODY_BYTES);
		assertThat(wrapper.getContentLength()).isEqualTo(BODY_BYTES.length);
		assertThat(wrapper.getContentLengthLong()).isEqualTo(BODY_BYTES.length);
	}

	// --- headers ---

	@Test
	void addHeaderWithSingleValue() {
		wrapper.addHeader(HttpHeaders.USER_AGENT, "test");
		assertThat(HttpServletUtils.headers(wrapper).getFirst(HttpHeaders.USER_AGENT)).isEqualTo("test");
	}

	@Test
	void addHeaderWithList() {
		wrapper.addHeader(HttpHeaders.ACCEPT, List.of("text/html", "application/json"));
		HttpHeaders headers = HttpServletUtils.headers(wrapper);
		assertThat(headers.get(HttpHeaders.ACCEPT)).contains("text/html", "application/json");
	}

	@Test
	void addHeaderWithArray() {
		wrapper.addHeader(HttpHeaders.ACCEPT, new String[] { "text/html", "application/json" });
		HttpHeaders headers = HttpServletUtils.headers(wrapper);
		assertThat(headers.get(HttpHeaders.ACCEPT)).contains("text/html", "application/json");
	}

	// --- parameters ---

	@Test
	void addParameterWithSingleValue() {
		wrapper.addParameter("password", "123456");
		assertThat(wrapper.getParameter("password")).isEqualTo("123456");
	}

	@Test
	void addParameterWithArray() {
		wrapper.addParameter("username", new String[] { "livk", "root", "admin" });
		assertThat(wrapper.getParameterValues("username")).containsExactly("livk", "root", "admin");
	}

	@Test
	void addParameterWithList() {
		wrapper.addParameter("tags", List.of("a", "b", "c"));
		assertThat(wrapper.getParameterValues("tags")).containsExactly("a", "b", "c");
	}

	@Test
	void getParameterMapReturnsAllParameters() {
		wrapper.addParameter("username", "livk");
		wrapper.addParameter("password", "123456");
		Map<String, String[]> paramMap = wrapper.getParameterMap();
		assertThat(paramMap).containsKey("username").containsKey("password");
		assertThat(paramMap.get("username")).containsExactly("livk");
	}

	@Test
	void getParameterReturnsNullForMissingKey() {
		assertThat(wrapper.getParameter("nonexistent")).isNull();
	}

}
