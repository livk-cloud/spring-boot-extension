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
import com.livk.commons.util.BeanUtils;
import com.livk.commons.util.HttpReactiveUtils;
import com.livk.context.fastexcel.ExcelDataType;
import com.livk.context.fastexcel.annotation.ExcelParam;
import com.livk.context.fastexcel.annotation.RequestExcel;
import com.livk.context.fastexcel.listener.ExcelMapReadListener;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
				&& this.canRead(resolvableType, exchange.getRequest().getHeaders().getContentType())) {
			mono = HttpReactiveUtils.getPartRequest(excelParam.fileName(), exchange).flatMap(request -> {
				ExcelMapReadListener<?> listener = BeanUtils.instantiateClass(requestExcel.parse());
				ResolvableType elementType = Objects.equals(resolvableType.resolve(), Mono.class)
						? resolvableType.getGeneric(0) : resolvableType;
				if (elementType.getRawClass() != null) {
					ExcelDataType dataType = Flux.class.isAssignableFrom(elementType.getRawClass())
							? ExcelDataType.COLLECTION : ExcelDataType.match(elementType.getRawClass());
					Class<?> excelModelClass = dataType.getFunction().apply(elementType);
					return Mono.just(request.getBody())
						.flatMap(DataBufferUtils::transform)
						.doOnSuccess(in -> listener.execute(in, excelModelClass, requestExcel.ignoreEmptyRow()))
						.map(in -> listener.getData(dataType));
				}
				return Mono.empty();
			});
		}

		return (adapter != null ? Mono.just(adapter.fromPublisher(mono)) : Mono.from(mono));
	}

	private boolean canRead(@NonNull ResolvableType elementType, MediaType mediaType) {
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
		catch (Exception ex) {
			return false;
		}
		return mediaType.toString().startsWith(MediaType.MULTIPART_FORM_DATA_VALUE);
	}

}
