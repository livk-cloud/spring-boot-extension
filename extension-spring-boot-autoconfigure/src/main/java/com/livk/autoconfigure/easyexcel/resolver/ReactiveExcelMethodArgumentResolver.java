/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.autoconfigure.easyexcel.resolver;

import com.livk.autoconfigure.easyexcel.annotation.ExcelImport;
import com.livk.autoconfigure.easyexcel.annotation.ExcelParam;
import com.livk.autoconfigure.easyexcel.listener.ExcelMapReadListener;
import com.livk.autoconfigure.easyexcel.utils.EasyExcelUtils;
import com.livk.autoconfigure.easyexcel.utils.ExcelDataType;
import com.livk.commons.bean.util.BeanUtils;
import com.livk.commons.io.DataBufferUtils;
import com.livk.commons.io.FileUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.http.codec.multipart.Part;
import org.springframework.lang.NonNull;
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
        return parameter.hasMethodAnnotation(ExcelImport.class) &&
               parameter.hasParameterAnnotation(ExcelParam.class);
    }

    @NonNull
    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, @NonNull BindingContext bindingContext, @NonNull ServerWebExchange exchange) {
        Class<?> resolvedType = ResolvableType.forMethodParameter(parameter).resolve();
        ReactiveAdapter adapter = (resolvedType != null ? adapterRegistry.getAdapter(resolvedType) : null);
        ExcelImport excelImport = parameter.getMethodAnnotation(ExcelImport.class);
        ExcelParam excelParam = parameter.getParameterAnnotation(ExcelParam.class);
        Mono<?> mono = Mono.empty();
        if (Objects.nonNull(excelImport) && Objects.nonNull(excelParam)) {
            ExcelMapReadListener<?> listener = BeanUtils.instantiateClass(excelImport.parse());
            ResolvableType genericType = ResolvableType.forMethodParameter(parameter);
            if (parameter.getParameterType().equals(Mono.class)) {
                genericType = genericType.getGeneric(0);
            }
            if (genericType.getRawClass() != null) {

                ExcelDataType dataType = Flux.class.isAssignableFrom(genericType.getRawClass()) ?
                        ExcelDataType.COLLECTION : ExcelDataType.match(genericType.getRawClass());
                Class<?> excelModelClass = dataType.getFunction().apply(genericType);
                mono = FileUtils.getPartValues(excelParam.fileName(), exchange)
                        .map(Part::content)
                        .flatMap(DataBufferUtils::transform)
                        .doOnSuccess(in -> EasyExcelUtils.read(in, excelModelClass, listener, excelImport.ignoreEmptyRow()))
                        .map(in -> listener.getData(dataType));
            }
        }
        return (adapter != null ? Mono.just(adapter.fromPublisher(mono)) : Mono.from(mono));
    }
}
