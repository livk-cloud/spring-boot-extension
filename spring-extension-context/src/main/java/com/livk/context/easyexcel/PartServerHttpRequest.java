/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.easyexcel;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;

/**
 * @author livk
 * @see org.springframework.web.reactive.result.method.annotation.RequestPartMethodArgumentResolver.PartServerHttpRequest
 */
public class PartServerHttpRequest extends ServerHttpRequestDecorator {

	private final Part part;

	public PartServerHttpRequest(ServerHttpRequest delegate, Part part) {
		super(delegate);
		this.part = part;
	}

	@NonNull
	public HttpHeaders getHeaders() {
		return this.part.headers();
	}

	@NonNull
	public Flux<DataBuffer> getBody() {
		return this.part.content();
	}

}
