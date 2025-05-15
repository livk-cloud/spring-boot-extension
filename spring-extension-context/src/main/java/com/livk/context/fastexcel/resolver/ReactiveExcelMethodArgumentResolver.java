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

package com.livk.context.fastexcel.resolver;

import com.livk.commons.io.DataBufferUtils;
import com.livk.commons.io.FileUtils;
import com.livk.commons.util.BeanUtils;
import com.livk.context.fastexcel.ExcelDataType;
import com.livk.context.fastexcel.annotation.ExcelParam;
import com.livk.context.fastexcel.annotation.RequestExcel;
import com.livk.context.fastexcel.listener.ExcelMapReadListener;
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
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * ReactiveExcelMethodArgumentResolver
 * </p>
 *
 * @author livk
 */
public class ReactiveExcelMethodArgumentResolver implements HandlerMethodArgumentResolver {

	private final ReactiveAdapterRegistry adapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

	private final ExcelHttpMessageReader reader = new ExcelHttpMessageReader();

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasMethodAnnotation(RequestExcel.class) && parameter.hasParameterAnnotation(ExcelParam.class);
	}

	@NonNull
	@Override
	public Mono<Object> resolveArgument(@NonNull MethodParameter parameter, @NonNull BindingContext bindingContext,
			@NonNull ServerWebExchange exchange) {
		Class<?> resolvedType = ResolvableType.forMethodParameter(parameter).resolve();
		ReactiveAdapter adapter = (resolvedType != null ? adapterRegistry.getAdapter(resolvedType) : null);
		RequestExcel requestExcel = parameter.getMethodAnnotation(RequestExcel.class);
		ExcelParam excelParam = parameter.getParameterAnnotation(ExcelParam.class);
		ResolvableType resolvableType = ResolvableType.forMethodParameter(parameter);
		Mono<?> mono = Mono.empty();
		if (requestExcel != null && excelParam != null
				&& reader.canRead(resolvableType, exchange.getRequest().getHeaders().getContentType())) {
			mono = FileUtils.getPartValues(excelParam.fileName(), exchange).flatMap(part -> {
				ExcelMapReadListener<?> listener = BeanUtils.instantiateClass(requestExcel.parse());
				Map<String, Object> hints = new HashMap<>();
				hints.put("listener", listener);
				hints.put("requestExcel", requestExcel);
				PartServerHttpRequest request = new PartServerHttpRequest(exchange.getRequest(), part);
				return reader.readMono(resolvableType, request, hints);
			});
		}

		return (adapter != null ? Mono.just(adapter.fromPublisher(mono)) : Mono.from(mono));
	}

	private static class ExcelHttpMessageReader implements HttpMessageReader<Object> {

		@NonNull
		@Override
		public List<MediaType> getReadableMediaTypes() {
			return List.of(MediaType.MULTIPART_FORM_DATA);
		}

		@Override
		public boolean canRead(@NonNull ResolvableType elementType, MediaType mediaType) {
			try {
				ResolvableType type = elementType;
				if (Objects.equals(type.resolve(), Mono.class)) {
					type = elementType.getGeneric(0);
				}
				if (type.getRawClass() == null) {
					return false;
				}
				if (Flux.class.isAssignableFrom(type.getRawClass())) {
					return true;
				}
				ExcelDataType.match(type.getRawClass());
			}
			catch (Exception e) {
				return false;
			}
			return mediaType.toString().startsWith(MediaType.MULTIPART_FORM_DATA_VALUE);
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
			ExcelMapReadListener<?> listener = (ExcelMapReadListener<?>) hints.get("listener");
			RequestExcel requestExcel = (RequestExcel) hints.get("requestExcel");
			if (Objects.equals(elementType.resolve(), Mono.class)) {
				elementType = elementType.getGeneric(0);
			}
			if (elementType.getRawClass() != null) {

				ExcelDataType dataType = Flux.class.isAssignableFrom(elementType.getRawClass())
						? ExcelDataType.COLLECTION : ExcelDataType.match(elementType.getRawClass());
				Class<?> excelModelClass = dataType.getFunction().apply(elementType);
				return Mono.just(message.getBody())
					.flatMap(DataBufferUtils::transform)
					.doOnSuccess(in -> listener.execute(in, excelModelClass, requestExcel.ignoreEmptyRow()))
					.map(in -> listener.getData(dataType));
			}
			return Mono.empty();
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
