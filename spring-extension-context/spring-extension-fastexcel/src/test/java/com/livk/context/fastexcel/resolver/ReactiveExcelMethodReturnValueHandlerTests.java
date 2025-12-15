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
import org.springframework.http.ContentDisposition;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.Map;

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
	void handleResultWithFluxShouldReturnExcel() {
		ExcelMapReadListener<Info> listener = new TypeExcelMapReadListener<>();
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/path?name=foo"));
		handler.handleResult(exchange, result).block(Duration.ofSeconds(5));

		Mono<List<Info>> mono = DataBufferUtils.transform(exchange.getResponse().getBody())
			.doOnSuccess(in -> listener.execute(in, Info.class, true))
			.map(in -> listener.toListData());

		StepVerifier.create(mono).expectNext(List.of(new Info("123456789"), new Info("987654321"))).verifyComplete();

		assertThat(exchange.getResponse().getHeaders().getContentDisposition())
			.isEqualTo(ContentDisposition.parse("attachment;filename=file.xlsm"));
	}

	@Test
	void handleResultWithMonoListShouldReturnExcel() throws Exception {
		// Prepare test data
		List<Info> expectedList = List.of(new Info("111"), new Info("222"));
		Mono<List<Info>> monoList = Mono.just(expectedList);

		// Create handler method with proper return type
		Method method = Info.class.getMethod("getMonoList");
		HandlerMethod handlerMethod = new HandlerMethod(new Info(), method);

		// Create HandlerResult with Mono<List<Info>>
		HandlerResult monoResult = new HandlerResult(handlerMethod, monoList, handlerMethod.getReturnType());

		// Execute
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/mono-list"));
		handler.handleResult(exchange, monoResult).block(Duration.ofSeconds(5));

		// Verify response body
		ExcelMapReadListener<Info> listener = new TypeExcelMapReadListener<>();
		Mono<List<Info>> resultMono = DataBufferUtils.transform(exchange.getResponse().getBody())
			.doOnSuccess(in -> listener.execute(in, Info.class, true))
			.map(in -> listener.toListData());

		StepVerifier.create(resultMono).expectNext(expectedList).verifyComplete();
	}

	@Test
	void handleResultWithMonoMapShouldReturnExcelWithMultipleSheets() throws Exception {
		// Prepare test data for multiple sheets
		Map<String, List<Info>> sheetData = Map.of("Sheet1", List.of(new Info("A1"), new Info("A2")), "Sheet2",
				List.of(new Info("B1"), new Info("B2")));
		Mono<Map<String, List<Info>>> monoMap = Mono.just(sheetData);

		// Create handler method with proper return type
		Method method = Info.class.getMethod("getMonoMap");
		HandlerMethod handlerMethod = new HandlerMethod(new Info(), method);

		// Create HandlerResult with Mono<Map<String, List<Info>>>
		HandlerResult monoResult = new HandlerResult(handlerMethod, monoMap, handlerMethod.getReturnType());

		// Execute
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/mono-map"));
		handler.handleResult(exchange, monoResult).block(Duration.ofSeconds(5));

		assertThat(exchange.getResponse().getHeaders().getContentDisposition())
			.isEqualTo(ContentDisposition.parse("attachment;filename=file.xlsm"));
	}

}
