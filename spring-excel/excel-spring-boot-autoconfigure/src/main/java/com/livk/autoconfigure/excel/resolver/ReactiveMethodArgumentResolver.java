package com.livk.autoconfigure.excel.resolver;

import com.alibaba.excel.EasyExcel;
import com.livk.autoconfigure.excel.annotation.ExcelImport;
import com.livk.autoconfigure.excel.listener.ExcelReadListener;
import com.livk.util.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * ReactiveMethodArgumentResolver
 * </p>
 *
 * @author livk
 * @date 2022/10/28
 */
public class ReactiveMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final ReactiveAdapterRegistry adapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        ExcelImport excelImport = parameter.getMethodAnnotation(ExcelImport.class);
        return excelImport != null && excelImport.paramName().equals(parameter.getParameterName());
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {
        Class<?> excelModelClass;
        if (parameter.getParameterType().equals(Mono.class)) {
            excelModelClass = ResolvableType.forMethodParameter(parameter).getGeneric(0).resolveGeneric(0);
        } else {
            if (!List.of(parameter.getParameterType().getInterfaces()).contains(Collection.class)) {
                throw new IllegalArgumentException(
                        "Excel upload request resolver error, @ExcelData parameter is not Collection ");
            }
            excelModelClass = ResolvableType.forMethodParameter(parameter).resolveGeneric(0);
        }
        Class<?> resolvedType = ResolvableType.forMethodParameter(parameter).resolve();
        ReactiveAdapter adapter = (resolvedType != null ? adapterRegistry.getAdapter(resolvedType) : null);
        ExcelImport importExcel = parameter.getMethodAnnotation(ExcelImport.class);
        Mono<Collection<?>> mono = Mono.empty();
        if (Objects.nonNull(importExcel)) {
            ExcelReadListener<?> listener = BeanUtils.instantiateClass(importExcel.parse());
            mono = getPartValues(importExcel.fileName(), exchange)
                    .flatMap(part -> DataBufferUtils.join(part.content())
                            .map(dataBuffer -> dataBuffer.toByteBuffer().array())
                            .map(ByteArrayInputStream::new)
                            .doOnSuccess(in -> EasyExcel.read(in, excelModelClass, listener)
                                    .ignoreEmptyRow(importExcel.ignoreEmptyRow())
                                    .sheet()
                                    .doRead())
                            .flatMap(in -> Mono.just(listener.getCollectionData()))
                    );
        }
        return (adapter != null ? Mono.just(adapter.fromPublisher(mono)) : Mono.from(mono));
    }

    public Mono<Part> getPartValues(String name, ServerWebExchange exchange) {
        return exchange.getMultipartData()
                .mapNotNull(multiValueMap -> multiValueMap.getFirst(name));
    }
}
