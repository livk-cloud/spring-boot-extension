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

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class RequestWrapperTests {

	RequestWrapper wrapper;

	@BeforeEach
	void init() {
		wrapper = new RequestWrapper(new MockHttpServletRequest());
	}

	@Test
	void body() throws IOException {
		Map<String, String> map = Map.of("root", "root");
		wrapper.body(JsonMapperUtils.writeValueAsBytes(map));

		assertThat(wrapper.getContentAsByteArray()).isEqualTo(JsonMapperUtils.writeValueAsBytes(map));
		assertThat(wrapper.getContentAsString()).isEqualTo(JsonMapperUtils.writeValueAsString(map));
		assertThat(wrapper.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
	}

	@Test
	void addHeader() {
		wrapper.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		wrapper.addHeader(HttpHeaders.USER_AGENT, "test");
		wrapper.addHeader(HttpHeaders.AGE, "20");

		HttpHeaders headers = HttpServletUtils.headers(wrapper);
		assertThat(headers.getFirst(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
		assertThat(headers.getFirst(HttpHeaders.USER_AGENT)).isEqualTo("test");
		assertThat(headers.getFirst(HttpHeaders.AGE)).isEqualTo("20");
		assertThat(headers.size()).isEqualTo(3);
	}

	@Test
	void addParameter() {
		wrapper.addParameter("username", new String[] { "livk", "root", "admin" });
		wrapper.addParameter("password", "123456");

		HttpParameters parameters = HttpServletUtils.params(wrapper);
		assertThat(parameters.get("username")).containsExactly("livk", "root", "admin");
		assertThat(parameters.get("password")).containsExactly("123456");
		assertThat(parameters).hasSize(2);
	}

}
