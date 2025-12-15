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

package com.livk.context.fastexcel.resolver;

import com.livk.context.fastexcel.Info;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class ExcelMethodReturnValueHandlerTests {

	static ExcelMethodReturnValueHandler handler;

	static MethodParameter parameter;

	@BeforeAll
	static void init() throws NoSuchMethodException {
		handler = new ExcelMethodReturnValueHandler();
		parameter = MethodParameter.forExecutable(Info.class.getDeclaredMethod("resolveResponse"), -1);
	}

	@Test
	void supportsReturnType() {
		assertThat(handler.supportsReturnType(parameter)).isTrue();
	}

	@Test
	void handleReturnValue() throws IOException {
		List<Info> list = List.of(new Info("123456789"), new Info("987654321"));
		ModelAndViewContainer mavContainer = new ModelAndViewContainer();
		MockHttpServletResponse response = new MockHttpServletResponse();

		assertThat(response.getContentAsByteArray()).isEmpty();

		ServletWebRequest webRequest = new ServletWebRequest(new MockHttpServletRequest(), response);
		handler.handleReturnValue(list, parameter, mavContainer, webRequest);

		assertThat(response.getContentAsByteArray()).isNotEmpty();
		assertThat(response.getHeader("Content-Disposition")).contains("attachment;filename=file.xlsm");
	}

	@Test
	void handleReturnValueWithMap() throws Exception {
		// Prepare test data with multiple sheets
		Map<String, List<Info>> sheetData = Map.of("Sheet1", List.of(new Info("A1"), new Info("A2")), "Sheet2",
				List.of(new Info("B1"), new Info("B2")));

		// Create method parameter for a method that returns Map
		MethodParameter mapParameter = MethodParameter.forExecutable(Info.class.getDeclaredMethod("resolveResponseMap"),
				-1);

		ModelAndViewContainer mavContainer = new ModelAndViewContainer();
		MockHttpServletResponse response = new MockHttpServletResponse();

		assertThat(response.getContentAsByteArray()).isEmpty();

		// Execute
		ServletWebRequest webRequest = new ServletWebRequest(new MockHttpServletRequest(), response);
		handler.handleReturnValue(sheetData, mapParameter, mavContainer, webRequest);

		// Verify
		assertThat(response.getContentAsByteArray()).isNotEmpty();
		assertThat(response.getHeader("Content-Disposition")).contains("attachment;filename=file.xlsm");
	}

}
