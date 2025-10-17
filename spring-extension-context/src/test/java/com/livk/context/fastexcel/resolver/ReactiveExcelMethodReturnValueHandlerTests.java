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

import com.livk.commons.io.DataBufferUtils;
import com.livk.context.fastexcel.Info;
import com.livk.context.fastexcel.listener.ExcelMapReadListener;
import com.livk.context.fastexcel.listener.TypeExcelMapReadListener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class ReactiveExcelMethodReturnValueHandlerTests {

	static ReactiveExcelMethodReturnValueHandler handler;

	static HandlerResult result;

	@BeforeAll
	static void init() throws NoSuchMethodException {
		handler = new ReactiveExcelMethodReturnValueHandler();

		Flux<Info> value = Flux.just(new Info("123456789"), new Info("987654321"));
		HandlerMethod handlerMethod = new HandlerMethod(new Info(),
				Info.class.getDeclaredMethod("resolveResponseReactive"));
		result = new HandlerResult(handlerMethod, value, handlerMethod.getReturnType());
	}

	@Test
	void supports() {
		assertThat(handler.supports(result)).isTrue();
	}

	@Test
	void handleResult() {
		ExcelMapReadListener<Info> listener = new TypeExcelMapReadListener<>();
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/path?name=foo"));
		handler.handleResult(exchange, result).block(Duration.ofSeconds(5));

		Mono<Collection<Info>> mono = DataBufferUtils.transform(exchange.getResponse().getBody())
			.doOnSuccess(in -> listener.execute(in, Info.class, true))
			.map(in -> listener.getCollectionData());

		StepVerifier.create(mono).expectNext(List.of(new Info("123456789"), new Info("987654321"))).verifyComplete();
	}

}
