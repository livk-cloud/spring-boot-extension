package com.livk.excel.mvc.resolver;

import com.livk.excel.annotation.ExcelImport;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartRequest;

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
        ExcelImport excelImport = parameter.getMethodAnnotation(ExcelImport.class);
        return excelImport != null && excelImport.paramName().equals(parameter.getParameterName());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (!List.of(parameter.getParameterType().getInterfaces()).contains(Collection.class)) {
            throw new IllegalArgumentException("Excel upload request resolver error, @ExcelData parameter is not Collection ");
        }
        var importExcel = parameter.getMethodAnnotation(ExcelImport.class);
        if (Objects.nonNull(importExcel)) {
            var listener = BeanUtils.instantiateClass(importExcel.parse());
            var request = webRequest.getNativeRequest(HttpServletRequest.class);
            Class<?> excelModelClass = ResolvableType.forMethodParameter(parameter).resolveGeneric(0);
            return listener.parse(getInputStream(request, importExcel.fileName()), excelModelClass).getCollectionData();
        }
        return null;
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
