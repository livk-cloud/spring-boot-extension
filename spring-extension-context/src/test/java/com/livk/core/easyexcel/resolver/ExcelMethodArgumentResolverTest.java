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

package com.livk.core.easyexcel.resolver;

import com.livk.core.easyexcel.Info;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class ExcelMethodArgumentResolverTest {

	static MethodParameter parameter;

	static ExcelMethodArgumentResolver resolver;

	@BeforeAll
	static void init() throws NoSuchMethodException {
		parameter = MethodParameter.forExecutable(Info.class.getDeclaredMethod("resolveRequest", List.class), 0);

		resolver = new ExcelMethodArgumentResolver();
	}

	@Test
	void supportsParameter() {
		assertTrue(resolver.supportsParameter(parameter));
	}

	@Test
	void resolveArgument() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", new ClassPathResource("outFile.xls").getInputStream());
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		request.addFile(file);

		ServletWebRequest webRequest = new ServletWebRequest(request);

		Object result = resolver.resolveArgument(parameter, null, webRequest, null);

		assertInstanceOf(List.class, result);

		@SuppressWarnings("unchecked")
		List<Info> list = (List<Info>) result;

		assertEquals(100, list.size());
	}

}
