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

import com.livk.commons.io.DataBufferConverter;
import com.livk.context.qrcode.PicType;
import com.livk.context.qrcode.QrCodeEntity;
import com.livk.context.qrcode.QrCodeManager;
import com.livk.context.qrcode.annotation.ResponseQrCode;
import com.livk.context.qrcode.exception.QrCodeException;
import com.livk.context.qrcode.support.GoogleQrCodeManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
	void supports() throws NoSuchMethodException {
		assertThat(handler.supports(result)).isTrue();

		HandlerMethod entityMethod = new HandlerMethod(new TestController(),
				TestController.class.getDeclaredMethod("entity"));
		HandlerResult entityResult = new HandlerResult(entityMethod, QrCodeEntity.builder("entity").build(),
				entityMethod.getReturnType());
		assertThat(handler.supports(entityResult)).isTrue();

		HandlerMethod plain = new HandlerMethod(new TestController(), TestController.class.getDeclaredMethod("plain"));
		HandlerResult plainResult = new HandlerResult(plain, "x", plain.getReturnType());
		assertThat(handler.supports(plainResult)).isFalse();
	}

	@Test
	void handleResult() {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/path?name=foo"));
		handler.handleResult(exchange, result).block(Duration.ofSeconds(5));

		Mono<String> mono = DataBufferConverter.transform(exchange.getResponse().getBody()).map(manager::parser);

		StepVerifier.create(mono).expectNext("code").verifyComplete();
		assertThat(exchange.getResponse().getHeaders().getContentType()).isEqualTo(MediaType.IMAGE_PNG);
	}

	@Test
	void handleResultWithMonoAndJpg() throws NoSuchMethodException {
		HandlerMethod handlerMethod = new HandlerMethod(new TestController(),
				TestController.class.getDeclaredMethod("jpg"));
		HandlerResult jpgResult = new HandlerResult(handlerMethod, Mono.just("jpg-code"),
				handlerMethod.getReturnType());
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/jpg"));

		handler.handleResult(exchange, jpgResult).block(Duration.ofSeconds(5));

		assertThat(exchange.getResponse().getHeaders().getContentType()).isEqualTo(MediaType.IMAGE_JPEG);
		StepVerifier.create(DataBufferConverter.transform(exchange.getResponse().getBody()).map(manager::parser))
			.expectNext("jpg-code")
			.verifyComplete();
	}

	@Test
	void handleResultWithQrCodeEntity() throws NoSuchMethodException {
		HandlerMethod handlerMethod = new HandlerMethod(new TestController(),
				TestController.class.getDeclaredMethod("entity"));
		HandlerResult entityResult = new HandlerResult(handlerMethod, QrCodeEntity.builder("entity-code").build(),
				handlerMethod.getReturnType());
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/entity"));

		handler.handleResult(exchange, entityResult).block(Duration.ofSeconds(5));
		StepVerifier.create(DataBufferConverter.transform(exchange.getResponse().getBody()).map(manager::parser))
			.expectNext("entity-code")
			.verifyComplete();
	}

	@Test
	void handleResultRejectsUnsupportedReactiveType() throws NoSuchMethodException {
		HandlerMethod handlerMethod = new HandlerMethod(new TestController(),
				TestController.class.getDeclaredMethod("flux"));
		HandlerResult fluxResult = new HandlerResult(handlerMethod, Flux.just("x"), handlerMethod.getReturnType());
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/flux"));

		assertThatExceptionOfType(QrCodeException.class)
			.isThrownBy(() -> handler.handleResult(exchange, fluxResult).block())
			.withMessageContaining("current type is not supported");
	}

	@Test
	void getOrderIsHighestPrecedence() {
		assertThat(handler.getOrder()).isEqualTo(Ordered.HIGHEST_PRECEDENCE);
	}

	@RestController
	private static final class TestController {

		@ResponseQrCode
		String qrcode() {
			return "code";
		}

		@ResponseQrCode(type = PicType.JPG)
		Mono<String> jpg() {
			return Mono.just("jpg-code");
		}

		QrCodeEntity<String> entity() {
			return QrCodeEntity.builder("entity-code").build();
		}

		@ResponseQrCode
		Flux<String> flux() {
			return Flux.just("x");
		}

		String plain() {
			return "x";
		}

	}

}
