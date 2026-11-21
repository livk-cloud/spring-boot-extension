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

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author livk
 */
class ResponseWrapperTests {

	ResponseWrapper wrapper;

	@BeforeEach
	void setUp() {
		wrapper = new ResponseWrapper(new MockHttpServletResponse());
	}

	// --- write via OutputStream ---

	@Test
	void writeViaOutputStreamAndReadBack() throws IOException {
		wrapper.getOutputStream().write("hello".getBytes(StandardCharsets.UTF_8));
		wrapper.getOutputStream().flush();
		assertThat(wrapper.getContentAsString()).isEqualTo("hello");
		assertThat(wrapper.getContentAsByteArray()).isEqualTo("hello".getBytes(StandardCharsets.UTF_8));
	}

	// --- write via Writer ---

	@Test
	void writeViaWriterAndReadBack() throws IOException {
		PrintWriter writer = wrapper.getWriter();
		writer.print("world");
		writer.flush();
		assertThat(wrapper.getContentAsString()).isEqualTo("world");
	}

	// --- getContentAsString with charset ---

	@Test
	void getContentAsStringWithCharset() throws IOException {
		byte[] utf8Bytes = "你好".getBytes(StandardCharsets.UTF_8);
		wrapper.getOutputStream().write(utf8Bytes);
		assertThat(wrapper.getContentAsString(StandardCharsets.UTF_8)).isEqualTo("你好");
	}

	// --- getCharacterEncoding ---

	@Test
	void getCharacterEncodingReturnsResponseEncoding() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		response.setCharacterEncoding("UTF-8");
		ResponseWrapper w = new ResponseWrapper(response);
		assertThat(w.getCharacterEncoding()).isEqualTo("UTF-8");
	}

	@Test
	void characterEncodingUsesDefaultWhenMissing() {
		HttpServletResponse response = mock(HttpServletResponse.class);
		given(response.getCharacterEncoding()).willReturn(null);
		ResponseWrapper w = new ResponseWrapper(response);
		assertThat(w.getCharacterEncoding()).isEqualTo(Charset.defaultCharset().name());
	}

	@Test
	void characterEncodingFallsBackWhenInvalid() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		response.setCharacterEncoding("not-a-real-charset");
		ResponseWrapper w = new ResponseWrapper(response);
		assertThat(w.getCharacterEncoding()).isEqualTo(Charset.defaultCharset().name());
	}

	@Test
	void getWriterReturnsSameInstance() throws IOException {
		assertThat(wrapper.getWriter()).isSameAs(wrapper.getWriter());
	}

	@Test
	void setWriteListenerThrowsUnsupportedOperationException() {
		assertThatThrownBy(() -> wrapper.getOutputStream().setWriteListener(null))
			.isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	void outputStreamIsReadyAndToStringMatchesBuffer() throws IOException {
		wrapper.getOutputStream().write("ready".getBytes(StandardCharsets.UTF_8));
		assertThat(wrapper.getOutputStream().isReady()).isTrue();
		assertThat(wrapper.getOutputStream().toString()).isEqualTo("ready");
	}

	// --- replaceBody(byte[]) ---

	@Test
	void replaceBodyWithBytesReplacesContent() throws IOException {
		wrapper.getOutputStream().write("original".getBytes(StandardCharsets.UTF_8));
		wrapper.replaceBody("replaced".getBytes(StandardCharsets.UTF_8));
		assertThat(wrapper.getContentAsString()).isEqualTo("replaced");
	}

	// --- replaceBody(String) ---

	@Test
	void replaceBodyWithStringReplacesContent() throws IOException {
		wrapper.getOutputStream().write("original".getBytes(StandardCharsets.UTF_8));
		wrapper.replaceBody("replaced");
		assertThat(wrapper.getContentAsString()).isEqualTo("replaced");
	}

	// --- replaceBody(String, Charset) ---

	@Test
	void replaceBodyWithStringAndCharsetReplacesContent() throws IOException {
		wrapper.getOutputStream().write("original".getBytes(StandardCharsets.UTF_8));
		wrapper.replaceBody("replaced", StandardCharsets.UTF_8);
		assertThat(wrapper.getContentAsByteArray()).isEqualTo("replaced".getBytes(StandardCharsets.UTF_8));
	}

	// --- reset ---

	@Test
	void resetClearsBuffer() throws IOException {
		wrapper.getOutputStream().write("data".getBytes(StandardCharsets.UTF_8));
		assertThat(wrapper.getContentAsByteArray()).isNotEmpty();
		wrapper.reset();
		assertThat(wrapper.getContentAsByteArray()).isEmpty();
	}

}
