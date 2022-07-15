package com.livk.excel.mvc.resolver;

import com.alibaba.excel.EasyExcel;
import com.livk.excel.annotation.ExcelImport;
import com.livk.util.BeanUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
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

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (!List.of(parameter.getParameterType().getInterfaces()).contains(Collection.class)) {
            throw new IllegalArgumentException(
                    "Excel upload request resolver error, @ExcelData parameter is not Collection ");
        }
        var importExcel = parameter.getMethodAnnotation(ExcelImport.class);
        if (Objects.nonNull(importExcel)) {
            var listener = BeanUtils.instantiateClass(importExcel.parse());
            var request = webRequest.getNativeRequest(HttpServletRequest.class);
            var excelModelClass = ResolvableType.forMethodParameter(parameter).resolveGeneric(0);
            EasyExcel.read(getInputStream(request, importExcel.fileName()), excelModelClass, listener)
                    .ignoreEmptyRow(importExcel.ignoreEmptyRow()).sheet().doRead();
            return listener.getCollectionData();
        }
        return Collections.emptyList();
    }

    private InputStream getInputStream(HttpServletRequest request, String fileName) {
        try {
            if (request instanceof MultipartRequest) {
                var file = ((MultipartRequest) request).getFile(fileName);
                assert file != null;
                return file.getInputStream();
            } else {
                return request.getInputStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
