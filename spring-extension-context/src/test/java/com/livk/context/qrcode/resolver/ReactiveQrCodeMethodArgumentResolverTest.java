/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.qrcode.resolver;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.livk.context.qrcode.QrCodeManager;
import com.livk.context.qrcode.annotation.RequestQrCodeText;
import com.livk.context.qrcode.support.GoogleQrCodeManager;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class ReactiveQrCodeMethodArgumentResolverTest {

	static ReactiveQrCodeMethodArgumentResolver resolver;

	static MethodParameter parameter;

	static BindingContext bindContext;

	@BeforeAll
	static void init() throws NoSuchMethodException {
		JsonMapper mapper = JsonMapper.builder().build();
		QrCodeManager manager = GoogleQrCodeManager.of(mapper);
		resolver = new ReactiveQrCodeMethodArgumentResolver(manager);
		parameter = MethodParameter.forExecutable(TestController.class.getDeclaredMethod("textCode", Mono.class), 0);
		ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
		initializer.setConversionService(new DefaultFormattingConversionService());
		bindContext = new BindingContext(initializer);
	}

	@Test
	void supportsParameter() {
		assertTrue(resolver.supportsParameter(parameter));
	}

	@Test
	void resolveArgument() {
		MultipartBodyBuilder builder = new MultipartBodyBuilder();
		builder.part("file", new ClassPathResource("qrCode.png"));

		MultipartHttpMessageWriter writer = new MultipartHttpMessageWriter(ClientCodecConfigurer.create().getWriters());

		MockClientHttpRequest clientRequest = new MockClientHttpRequest(HttpMethod.POST, "/");
		writer
			.write(Mono.just(builder.build()), ResolvableType.forClass(MultiValueMap.class),
					MediaType.MULTIPART_FORM_DATA, clientRequest, Collections.emptyMap())
			.block();

		MediaType contentType = clientRequest.getHeaders().getContentType();
		Flux<DataBuffer> body = clientRequest.getBody();
		assertNotNull(contentType);
		MockServerHttpRequest serverRequest = MockServerHttpRequest.post("/").contentType(contentType).body(body);

		MockServerWebExchange exchange = MockServerWebExchange.from(serverRequest);

		Mono<Object> result = resolver.resolveArgument(parameter, bindContext, exchange);

		StepVerifier.create(result.flatMap(o -> {
			if (o instanceof Mono) {
				return (Mono<?>) o;
			}
			return Mono.just(o);
		})).expectNext("QrCode").verifyComplete();
	}

	static class TestController {

		Mono<String> textCode(@RequestQrCodeText Mono<String> text) {
			return text;
		}

	}

}
