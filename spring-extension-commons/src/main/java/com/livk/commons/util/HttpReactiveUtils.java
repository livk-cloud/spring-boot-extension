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

package com.livk.commons.util;

import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author livk
 */
@UtilityClass
public class HttpReactiveUtils {

	/**
	 * 从ServerWebExchange读取文件转成Mono Part
	 * @param name 文件参数
	 * @param exchange the exchange
	 * @return the part values
	 */
	public Mono<Part> getPartValues(String name, ServerWebExchange exchange) {
		return exchange.getMultipartData().mapNotNull(multiValueMap -> multiValueMap.getFirst(name));
	}

	public Mono<ServerHttpRequestDecorator> getPartRequest(String name, ServerWebExchange exchange) {
		return getPartValues(name, exchange).map(part -> new PartServerHttpRequest(exchange.getRequest(), part));
	}

	private static class PartServerHttpRequest extends ServerHttpRequestDecorator {

		private final Part part;

		PartServerHttpRequest(ServerHttpRequest delegate, Part part) {
			super(delegate);
			this.part = part;
		}

		@NonNull public HttpHeaders getHeaders() {
			return this.part.headers();
		}

		@NonNull public Flux<DataBuffer> getBody() {
			return this.part.content();
		}

	}

}
