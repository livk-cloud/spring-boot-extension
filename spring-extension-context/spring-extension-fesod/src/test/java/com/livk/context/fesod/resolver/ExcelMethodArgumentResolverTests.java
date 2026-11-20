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

package com.livk.context.fesod.resolver;

import com.livk.context.fesod.Info;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class ExcelMethodArgumentResolverTests {

	static MethodParameter parameter;

	static ExcelMethodArgumentResolver resolver;

	@BeforeAll
	static void init() throws NoSuchMethodException {
		parameter = MethodParameter.forExecutable(Info.class.getDeclaredMethod("resolveRequest", List.class), 0);

		resolver = new ExcelMethodArgumentResolver();
	}

	@Test
	void supportsParameter() {
		assertThat(resolver.supportsParameter(parameter)).isTrue();
	}

	@Test
	void supportsParameterReturnsFalseWithoutAnnotations() throws NoSuchMethodException {
		MethodParameter without = MethodParameter
			.forExecutable(Info.class.getDeclaredMethod("resolveWithoutAnnotations", List.class), 0);
		assertThat(resolver.supportsParameter(without)).isFalse();
	}

	@Test
	void resolveArgument() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", new ClassPathResource("outFile.xls").getInputStream());
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		request.addFile(file);

		ServletWebRequest webRequest = new ServletWebRequest(request);

		Object result = resolver.resolveArgument(parameter, null, webRequest, null);

		assertThat(result).isInstanceOf(List.class);

		@SuppressWarnings("unchecked")
		List<Info> list = (List<Info>) result;

		assertThat(list).hasSize(100);
	}

	@Test
	void resolveArgumentThrowsWhenContentTypeMissing() throws Exception {
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		request.setContentType(null);
		ServletWebRequest webRequest = new ServletWebRequest(request);

		assertThatThrownBy(() -> resolver.resolveArgument(parameter, null, webRequest, null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("@ExcelParam");
	}

	@Test
	void resolveArgumentThrowsWhenNotMultipart() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ServletWebRequest webRequest = new ServletWebRequest(request);

		assertThatThrownBy(() -> resolver.resolveArgument(parameter, null, webRequest, null))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void resolveArgumentThrowsWhenTypeUnsupported() throws Exception {
		MethodParameter unsupported = MethodParameter
			.forExecutable(Info.class.getDeclaredMethod("resolveRequestUnsupported", String.class), 0);
		MockMultipartFile file = new MockMultipartFile("file", new ClassPathResource("outFile.xls").getInputStream());
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		request.addFile(file);

		assertThatThrownBy(() -> resolver.resolveArgument(unsupported, null, new ServletWebRequest(request), null))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void resolveArgumentReadsMap() throws Exception {
		MethodParameter mapParameter = MethodParameter
			.forExecutable(Info.class.getDeclaredMethod("resolveRequestMap", Map.class), 0);
		MockMultipartFile file = new MockMultipartFile("file", new ClassPathResource("outFile.xls").getInputStream());
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		request.addFile(file);

		Object result = resolver.resolveArgument(mapParameter, null, new ServletWebRequest(request), null);

		assertThat(result).isInstanceOf(Map.class);
		@SuppressWarnings("unchecked")
		Map<String, List<Info>> map = (Map<String, List<Info>>) result;
		assertThat(map.values().stream().mapToInt(List::size).sum()).isEqualTo(100);
	}

}
