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

import com.livk.commons.io.DataBufferUtils;
import com.livk.commons.util.HttpReactiveUtils;
import com.livk.context.qrcode.QrCodeManager;
import com.livk.context.qrcode.annotation.RequestQrCodeText;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class ReactiveQrCodeMethodArgumentResolver implements HandlerMethodArgumentResolver {

	private final ReactiveAdapterRegistry adapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

	private final QrCodeManager codeManager;

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
		if (qrCodeText != null && this.canRead(resolvableType, exchange.getRequest().getHeaders().getContentType())) {
			mono = HttpReactiveUtils.getPartRequest(qrCodeText.fileName(), exchange)
				.flatMap(request -> Mono.just(request.getBody())
					.flatMap(DataBufferUtils::transformByte)
					.map(codeManager::parser));
		}

		return (adapter != null ? Mono.just(adapter.fromPublisher(mono)) : Mono.from(mono));
	}

	private boolean canRead(@NonNull ResolvableType elementType, MediaType mediaType) {
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

}
