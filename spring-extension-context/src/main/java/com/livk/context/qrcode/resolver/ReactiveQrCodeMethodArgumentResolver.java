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

import com.livk.commons.io.DataBufferUtils;
import com.livk.commons.io.FileUtils;
import com.livk.context.qrcode.QrCodeManager;
import com.livk.context.qrcode.annotation.RequestQrCodeText;
import com.livk.context.qrcode.support.QrCodeSupport;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author livk
 */
public class ReactiveQrCodeMethodArgumentResolver implements HandlerMethodArgumentResolver {

	private final ReactiveAdapterRegistry adapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

	private final QrCodeHttpMessageReader reader;

	public ReactiveQrCodeMethodArgumentResolver(QrCodeManager qrCodeManager) {
		this.reader = new QrCodeHttpMessageReader(qrCodeManager);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestQrCodeText.class);
	}

	@NonNull
	@Override
	public Mono<Object> resolveArgument(@NonNull MethodParameter parameter, @NonNull BindingContext bindingContext,
			@NonNull ServerWebExchange exchange) {
		Class<?> resolvedType = ResolvableType.forMethodParameter(parameter).resolve();
		ReactiveAdapter adapter = (resolvedType != null ? adapterRegistry.getAdapter(resolvedType) : null);
		RequestQrCodeText qrCodeText = parameter.getParameterAnnotation(RequestQrCodeText.class);
		ResolvableType resolvableType = ResolvableType.forMethodParameter(parameter);
		Mono<?> mono = Mono.empty();
		if (qrCodeText != null && reader.canRead(resolvableType, exchange.getRequest().getHeaders().getContentType())) {
			mono = FileUtils.getPartValues(qrCodeText.fileName(), exchange).flatMap(part -> {
				PartServerHttpRequest request = new PartServerHttpRequest(exchange.getRequest(), part);
				return reader.readMono(resolvableType, request, Map.of());
			});
		}

		return (adapter != null ? Mono.just(adapter.fromPublisher(mono)) : Mono.from(mono));
	}

	private static class QrCodeHttpMessageReader extends QrCodeSupport implements HttpMessageReader<Object> {

		private QrCodeHttpMessageReader(QrCodeManager qrCodeManager) {
			super(qrCodeManager);
		}

		@NonNull
		@Override
		public List<MediaType> getReadableMediaTypes() {
			return List.of(MediaType.MULTIPART_FORM_DATA);
		}

		@Override
		public boolean canRead(@NonNull ResolvableType elementType, MediaType mediaType) {
			if (mediaType == null || mediaType.toString().startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)) {
				ResolvableType type = elementType;
				// 检查是否为 Mono 类型
				if (Objects.equals(type.resolve(), Mono.class)) {
					type = elementType.getGeneric(0); // 获取 Mono 的泛型类型
				}
				// 检查是否为 String 类型
				return Objects.equals(type.resolve(), String.class);
			}
			return false;
		}

		@NonNull
		@Override
		public Flux<Object> read(@NonNull ResolvableType elementType, @NonNull ReactiveHttpInputMessage message,
				@NonNull Map<String, Object> hints) {
			throw new UnsupportedOperationException("");
		}

		@NonNull
		@Override
		public Mono<Object> readMono(@NonNull ResolvableType elementType, @NonNull ReactiveHttpInputMessage message,
				@NonNull Map<String, Object> hints) {
			return Mono.just(message.getBody()).flatMap(DataBufferUtils::transformByte).map(super::parser);
		}

	}

	/**
	 * @author livk
	 * @see org.springframework.web.reactive.result.method.annotation.RequestPartMethodArgumentResolver.PartServerHttpRequest
	 */
	private static class PartServerHttpRequest extends ServerHttpRequestDecorator {

		private final Part part;

		public PartServerHttpRequest(ServerHttpRequest delegate, Part part) {
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
