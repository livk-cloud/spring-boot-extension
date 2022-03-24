package com.livk.excel.reactive.resolver;

import com.livk.excel.annotation.ExcelImport;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * ExcelMethodArgumentResolver
 * </p>
 *
 * @author livk
 * @date 2022/1/17
 */
public class ExcelMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        var excelImport = parameter.getMethodAnnotation(ExcelImport.class);
        return excelImport != null && excelImport.paramName().equals(parameter.getParameterName());
    }

    @NonNull
    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, @NonNull BindingContext bindingContext, @NonNull ServerWebExchange exchange) {
        if (!List.of(parameter.getParameterType().getInterfaces()).contains(Collection.class)) {
            throw new IllegalArgumentException("Excel upload request resolver error, @ExcelData parameter is not Collection ");
        }
        var importExcel = parameter.getMethodAnnotation(ExcelImport.class);
        if (Objects.nonNull(importExcel)) {
            var listener = BeanUtils.instantiateClass(importExcel.parse());
            var request = exchange.getRequest();
            var excelModelClass = ResolvableType.forMethodParameter(parameter).resolveGeneric(0);
            return Mono.just(listener.parse(getInputStream(request, importExcel.fileName()), excelModelClass).getCollectionData());
        }
        return Mono.just(BeanUtils.instantiateClass(parameter.getParameterType()));
    }


    private InputStream getInputStream(ServerHttpRequest request, String fileName) {
        try {
            if (request instanceof MultipartRequest multipartRequest) {
                var file = multipartRequest.getFile(fileName);
                assert file != null;
                return file.getInputStream();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
