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
import org.springframework.core.ResolvableType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.multipart.MultipartHttpMessageWriter;
import org.springframework.mock.http.client.reactive.MockClientHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.reactive.BindingContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class ReactiveExcelMethodArgumentResolverTests {

	static ReactiveExcelMethodArgumentResolver resolver;

	static MethodParameter parameter;

	static BindingContext bindContext;

	static MethodParameter monoParameter;

	static MethodParameter listParameter;

	static MethodParameter monoMapParameter;

	static MethodParameter unsupportedParameter;

	@BeforeAll
	static void init() throws NoSuchMethodException {
		resolver = new ReactiveExcelMethodArgumentResolver();
		parameter = MethodParameter.forExecutable(Info.class.getDeclaredMethod("resolveRequestReactive", Flux.class),
				0);
		monoParameter = MethodParameter
			.forExecutable(Info.class.getDeclaredMethod("resolveRequestReactiveMono", Mono.class), 0);
		listParameter = MethodParameter
			.forExecutable(Info.class.getDeclaredMethod("resolveRequestReactiveList", List.class), 0);
		monoMapParameter = MethodParameter
			.forExecutable(Info.class.getDeclaredMethod("resolveRequestReactiveMonoMap", Mono.class), 0);
		unsupportedParameter = MethodParameter
			.forExecutable(Info.class.getDeclaredMethod("resolveWithoutAnnotations", List.class), 0);
		ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
		initializer.setConversionService(new DefaultFormattingConversionService());
		bindContext = new BindingContext(initializer);
	}

	@Test
	void supportsParameter() {
		assertThat(resolver.supportsParameter(parameter)).isTrue();
		assertThat(resolver.supportsParameter(monoParameter)).isTrue();
		assertThat(resolver.supportsParameter(listParameter)).isTrue();
		assertThat(resolver.supportsParameter(monoMapParameter)).isTrue();
		assertThat(resolver.supportsParameter(unsupportedParameter)).isFalse();
	}

	@Test
	@SuppressWarnings("unchecked")
	void resolveArgument() {
		MockServerWebExchange exchange = multipartExchange();

		Mono<Object> result = resolver.resolveArgument(parameter, bindContext, exchange);

		StepVerifier.create(result.filter(Flux.class::isInstance).flatMap(o -> {
			Flux<List<?>> flux = (Flux<List<?>>) o;
			return flux.flatMap(objects -> Flux.fromStream(objects.stream())).collectList();
		}).flatMapMany(Flux::fromIterable)).expectNextCount(100).verifyComplete();
	}

	@Test
	@SuppressWarnings("unchecked")
	void resolveArgumentForMonoList() {
		MockServerWebExchange exchange = multipartExchange();

		Mono<Object> result = resolver.resolveArgument(monoParameter, bindContext, exchange);

		StepVerifier.create(result.flatMap(o -> (Mono<List<Info>>) o)).assertNext((list) -> {
			assertThat(list).hasSize(100);
			assertThat(list.getFirst()).isInstanceOf(Info.class);
		}).verifyComplete();
	}

	@Test
	void resolveArgumentReturnsEmptyForUnsupportedContentType() {
		MockServerHttpRequest serverRequest = MockServerHttpRequest.post("/")
			.contentType(MediaType.APPLICATION_JSON)
			.body("[]");
		MockServerWebExchange exchange = MockServerWebExchange.from(serverRequest);

		// Flux 分支不检查 Content-Type；List 分支会因非 multipart 返回空
		StepVerifier.create(resolver.resolveArgument(listParameter, bindContext, exchange)).verifyComplete();
	}

	@Test
	@SuppressWarnings("unchecked")
	void resolveArgumentForListWithoutAdapter() {
		MockServerWebExchange exchange = multipartExchange();

		Mono<Object> result = resolver.resolveArgument(listParameter, bindContext, exchange);

		StepVerifier.create(result.cast(List.class)).assertNext((list) -> {
			assertThat(list).hasSize(100);
			assertThat(list.getFirst()).isInstanceOf(Info.class);
		}).verifyComplete();
	}

	@Test
	@SuppressWarnings("unchecked")
	void resolveArgumentForMonoMap() {
		MockServerWebExchange exchange = multipartExchange();

		Mono<Object> result = resolver.resolveArgument(monoMapParameter, bindContext, exchange);

		StepVerifier.create(result.flatMap(o -> (Mono<Map<String, List<Info>>>) o)).assertNext((map) -> {
			assertThat(map).isNotEmpty();
			assertThat(map.values().iterator().next()).hasSize(100);
		}).verifyComplete();
	}

	@Test
	void resolveArgumentReturnsEmptyForUnsupportedParameterType() throws NoSuchMethodException {
		MethodParameter bad = MethodParameter.forExecutable(
				ReactiveExcelMethodArgumentResolverTests.class.getDeclaredMethod("unsupportedExcelParam", Info.class),
				0);
		MockServerWebExchange exchange = multipartExchange();
		assertThat(resolver.supportsParameter(bad)).isTrue();
		StepVerifier.create(resolver.resolveArgument(bad, bindContext, exchange)).verifyComplete();
	}

	@SuppressWarnings("unused")
	@com.livk.context.fesod.annotation.RequestExcel
	void unsupportedExcelParam(@com.livk.context.fesod.annotation.ExcelParam Info info) {
	}

	private static MockServerWebExchange multipartExchange() {
		MultipartBodyBuilder builder = new MultipartBodyBuilder();
		builder.part("file", new ClassPathResource("outFile.xls"));

		MultipartHttpMessageWriter writer = new MultipartHttpMessageWriter(ClientCodecConfigurer.create().getWriters());

		MockClientHttpRequest clientRequest = new MockClientHttpRequest(HttpMethod.POST, "/");
		writer
			.write(Mono.just(builder.build()), ResolvableType.forClass(MultiValueMap.class),
					MediaType.MULTIPART_FORM_DATA, clientRequest, Collections.emptyMap())
			.block();

		MediaType contentType = clientRequest.getHeaders().getContentType();
		Flux<DataBuffer> body = clientRequest.getBody();
		MockServerHttpRequest serverRequest = MockServerHttpRequest.post("/").contentType(contentType).body(body);
		return MockServerWebExchange.from(serverRequest);
	}

}
