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

import com.livk.commons.io.FileUtils;
import com.livk.commons.util.BeanUtils;
import com.livk.context.easyexcel.PartServerHttpRequest;
import com.livk.context.easyexcel.annotation.ExcelParam;
import com.livk.context.easyexcel.annotation.RequestExcel;
import com.livk.context.easyexcel.converter.ExcelHttpMessageReader;
import com.livk.context.easyexcel.listener.ExcelMapReadListener;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

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

}
