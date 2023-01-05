package com.livk.autoconfigure.easyexcel.resolver;

import com.livk.autoconfigure.easyexcel.annotation.ExcelImport;
import com.livk.autoconfigure.easyexcel.listener.ExcelReadListener;
import com.livk.autoconfigure.easyexcel.utils.ExcelUtils;
import com.livk.commons.io.DataBufferUtils;
import com.livk.commons.io.FileUtils;
import com.livk.commons.util.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
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
        ExcelImport excelImport = parameter.getMethodAnnotation(ExcelImport.class);
        return excelImport != null && excelImport.paramName().equals(parameter.getParameterName());
    }

    @NonNull
    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, @NonNull BindingContext bindingContext, @NonNull ServerWebExchange exchange) {
        Class<?> excelModelClass;
        if (parameter.getParameterType().equals(Mono.class)) {
            excelModelClass = ResolvableType.forMethodParameter(parameter).getGeneric(0).resolveGeneric(0);
        } else {
            if (!Collection.class.isAssignableFrom(parameter.getParameterType())) {
                throw new IllegalArgumentException("Excel upload request resolver error, @ExcelData parameter is not Collection ");
            }
            excelModelClass = ResolvableType.forMethodParameter(parameter).resolveGeneric(0);
        }
        Class<?> resolvedType = ResolvableType.forMethodParameter(parameter).resolve();
        ReactiveAdapter adapter = (resolvedType != null ? adapterRegistry.getAdapter(resolvedType) : null);
        ExcelImport importExcel = parameter.getMethodAnnotation(ExcelImport.class);
        Mono<Collection<?>> mono = Mono.empty();
        if (Objects.nonNull(importExcel)) {
            ExcelReadListener<?> listener = BeanUtils.instantiateClass(importExcel.parse());
            mono = FileUtils.getPartValues(importExcel.fileName(), exchange)
                    .flatMap(DataBufferUtils::transform)
                    .doOnSuccess(in -> ExcelUtils.read(in, excelModelClass, listener, importExcel.ignoreEmptyRow()))
                    .map(in -> listener.getCollectionData());
        }
        return (adapter != null ? Mono.just(adapter.fromPublisher(mono)) : Mono.from(mono));
    }

}
