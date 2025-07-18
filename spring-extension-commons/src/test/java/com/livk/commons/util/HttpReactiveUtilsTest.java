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

package com.livk.commons.util;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author livk
 */
class HttpReactiveUtilsTest {

	@Test
	void getPartValues() {
		Part part = mock(Part.class);
		when(part.name()).thenReturn("file");

		// mock ServerWebExchange
		ServerWebExchange exchange = mock(ServerWebExchange.class);

		MultiValueMap<String, Part> partMap = new LinkedMultiValueMap<>();
		partMap.add("file", part);

		when(exchange.getMultipartData()).thenReturn(Mono.just(partMap));

		StepVerifier.create(HttpReactiveUtils.getPartValues("file", exchange)).expectNext(part).verifyComplete();
	}

	@Test
	void getPartRequest() {
		String fileContent = "file content";
		DataBuffer buffer = new DefaultDataBufferFactory().wrap(fileContent.getBytes(StandardCharsets.UTF_8));

		// Mock Part
		Part mockPart = mock(Part.class);
		when(mockPart.headers()).thenReturn(new HttpHeaders());
		when(mockPart.content()).thenReturn(Flux.just(buffer));

		// Mock MultipartData
		LinkedMultiValueMap<String, Part> partMap = new LinkedMultiValueMap<>();
		partMap.add("file", mockPart);

		// Mock Exchange
		ServerHttpRequest originalRequest = MockServerHttpRequest.post("/upload").build();
		ServerWebExchange exchange = mock(ServerWebExchange.class);
		when(exchange.getRequest()).thenReturn(originalRequest);
		when(exchange.getMultipartData()).thenReturn(Mono.just(partMap));

		// Call method
		Mono<ServerHttpRequestDecorator> resultMono = HttpReactiveUtils.getPartRequest("file", exchange);

		// Verify
		StepVerifier.create(resultMono).assertNext(decorator -> {
			// headers must come from part
			assertEquals(mockPart.headers(), decorator.getHeaders());

			// body must contain expected content
			StepVerifier.create(decorator.getBody()).consumeNextWith(data -> {
				byte[] bytes = new byte[data.readableByteCount()];
				data.read(bytes);
				assertEquals(fileContent, new String(bytes, StandardCharsets.UTF_8));
			}).verifyComplete();
		}).verifyComplete();
	}

}
