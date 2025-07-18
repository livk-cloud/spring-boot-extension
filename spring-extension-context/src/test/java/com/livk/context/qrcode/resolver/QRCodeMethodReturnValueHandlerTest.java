/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.context.qrcode.resolver;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.livk.context.qrcode.QrCodeManager;
import com.livk.context.qrcode.annotation.ResponseQrCode;
import com.livk.context.qrcode.support.GoogleQrCodeManager;
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

	static QrCodeMethodReturnValueHandler handler;

	static MethodParameter parameter;

	static QrCodeManager manager;

	@BeforeAll
	static void init() throws NoSuchMethodException {
		JsonMapper mapper = JsonMapper.builder().build();
		manager = GoogleQrCodeManager.of(mapper);
		handler = new QrCodeMethodReturnValueHandler(manager);
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

		assertEquals("code", manager.parser(inputStream));
	}

	@ResponseQrCode
	String qrcode() {
		return "code";
	}

}
