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

package com.livk.core.qrcode.resolver;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.livk.core.qrcode.QRCodeGenerator;
import com.livk.core.qrcode.QRCodeUtils;
import com.livk.core.qrcode.annotation.ResponseQRCode;
import com.livk.core.qrcode.support.GoogleQRCodeGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class QRCodeMethodReturnValueHandlerTest {

	static QRCodeMethodReturnValueHandler handler;

	static MethodParameter parameter;

	@BeforeAll
	static void init() throws NoSuchMethodException {
		JsonMapper mapper = JsonMapper.builder().build();
		QRCodeGenerator generator = new GoogleQRCodeGenerator(mapper);
		handler = new QRCodeMethodReturnValueHandler(generator);
		parameter = MethodParameter.forExecutable(QRCodeMethodReturnValueHandlerTest.class.getDeclaredMethod("qrcode"),
				-1);
	}

	@Test
	void supportsReturnType() {
		assertTrue(handler.supportsReturnType(parameter));
	}

	@Test
	void handleReturnValue() throws IOException {
		ModelAndViewContainer mavContainer = new ModelAndViewContainer();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ServletWebRequest webRequest = new ServletWebRequest(new MockHttpServletRequest(), response);

		handler.handleReturnValue("code", parameter, mavContainer, webRequest);

		InputStream inputStream = new ByteArrayInputStream(response.getContentAsByteArray());

		assertEquals("code", QRCodeUtils.parseQRCode(inputStream));
	}

	@ResponseQRCode
	String qrcode() {
		return "code";
	}

}
