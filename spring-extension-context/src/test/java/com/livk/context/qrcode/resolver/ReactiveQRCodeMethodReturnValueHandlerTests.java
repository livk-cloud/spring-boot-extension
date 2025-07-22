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

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.livk.commons.io.DataBufferUtils;
import com.livk.context.qrcode.QrCodeManager;
import com.livk.context.qrcode.annotation.ResponseQrCode;
import com.livk.context.qrcode.support.GoogleQrCodeManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerResult;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class ReactiveQRCodeMethodReturnValueHandlerTests {

	static ReactiveQrCodeMethodReturnValueHandler handler;

	static HandlerResult result;

	static QrCodeManager manager;

	@BeforeAll
	static void init() throws NoSuchMethodException {
		JsonMapper mapper = JsonMapper.builder().build();
		manager = GoogleQrCodeManager.of(mapper);
		handler = new ReactiveQrCodeMethodReturnValueHandler(manager);
		HandlerMethod handlerMethod = new HandlerMethod(new TestController(),
				TestController.class.getDeclaredMethod("qrcode"));
		result = new HandlerResult(handlerMethod, "code", handlerMethod.getReturnType());
	}

	@Test
	void supports() {
		assertTrue(handler.supports(result));
	}

	@Test
	void handleResult() {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/path?name=foo"));
		handler.handleResult(exchange, result).block(Duration.ofSeconds(5));

		Mono<String> mono = DataBufferUtils.transform(exchange.getResponse().getBody()).map(manager::parser);

		StepVerifier.create(mono).expectNext("code").verifyComplete();
	}

	@RestController
	private static class TestController {

		@ResponseQrCode
		String qrcode() {
			return "code";
		}

	}

}
