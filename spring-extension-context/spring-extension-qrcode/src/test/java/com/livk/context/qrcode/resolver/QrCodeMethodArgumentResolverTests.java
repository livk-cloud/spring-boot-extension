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

package com.livk.context.qrcode.resolver;

import tools.jackson.databind.json.JsonMapper;
import com.livk.context.qrcode.QrCodeEntity;
import com.livk.context.qrcode.QrCodeManager;
import com.livk.context.qrcode.annotation.RequestQrCodeText;
import com.livk.context.qrcode.support.GoogleQrCodeManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class QrCodeMethodArgumentResolverTests {

	static MethodParameter parameter;

	static QrCodeMethodArgumentResolver resolver;

	@BeforeAll
	static void init() throws NoSuchMethodException {
		parameter = MethodParameter.forExecutable(TestController.class.getDeclaredMethod("textCode", String.class), 0);
		JsonMapper mapper = JsonMapper.builder().build();
		QrCodeManager manager = GoogleQrCodeManager.of(mapper);
		resolver = new QrCodeMethodArgumentResolver(manager);
	}

	@Test
	void supportsParameter() {
		assertThat(resolver.supportsParameter(parameter)).isTrue();
	}

	@Test
	void resolveArgument() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", new ClassPathResource("qrCode.png").getInputStream());
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		request.addFile(file);

		ServletWebRequest webRequest = new ServletWebRequest(request);

		Object result = resolver.resolveArgument(parameter, null, webRequest, null);

		assertThat(result).isEqualTo("QrCode");
	}

	static class TestController {

		QrCodeEntity<String> textCode(@RequestQrCodeText String text) {
			return QrCodeEntity.builder(text).build();
		}

	}

}
