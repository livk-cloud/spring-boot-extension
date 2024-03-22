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

package com.livk.context.easyexcel.resolver;

import java.util.Objects;

import com.livk.commons.io.DataBufferUtils;
import com.livk.commons.io.FileUtils;
import com.livk.commons.util.BeanUtils;
import com.livk.context.easyexcel.EasyExcelSupport;
import com.livk.context.easyexcel.ExcelDataType;
import com.livk.context.easyexcel.annotation.ExcelParam;
import com.livk.context.easyexcel.annotation.RequestExcel;
import com.livk.context.easyexcel.listener.ExcelMapReadListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.http.codec.multipart.Part;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;

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
		RequestExcel excelImport = parameter.getMethodAnnotation(RequestExcel.class);
		ExcelParam excelParam = parameter.getParameterAnnotation(ExcelParam.class);
		Mono<?> mono = Mono.empty();
		if (Objects.nonNull(excelImport) && Objects.nonNull(excelParam)) {
			ExcelMapReadListener<?> listener = BeanUtils.instantiateClass(excelImport.parse());
			ResolvableType genericType = ResolvableType.forMethodParameter(parameter);
			if (parameter.getParameterType().equals(Mono.class)) {
				genericType = genericType.getGeneric(0);
			}
			if (genericType.getRawClass() != null) {

				ExcelDataType dataType = Flux.class.isAssignableFrom(genericType.getRawClass())
						? ExcelDataType.COLLECTION : ExcelDataType.match(genericType.getRawClass());
				Class<?> excelModelClass = dataType.getFunction().apply(genericType);
				mono = FileUtils.getPartValues(excelParam.fileName(), exchange)
					.map(Part::content)
					.flatMap(DataBufferUtils::transform)
					.doOnSuccess(
							in -> EasyExcelSupport.read(in, excelModelClass, listener, excelImport.ignoreEmptyRow()))
					.map(in -> listener.getData(dataType));
			}
		}
		return (adapter != null ? Mono.just(adapter.fromPublisher(mono)) : Mono.from(mono));
	}

}
