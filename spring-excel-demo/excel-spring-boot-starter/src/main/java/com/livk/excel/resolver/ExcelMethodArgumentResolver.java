package com.livk.excel.resolver;

import com.livk.excel.annotation.ExcelData;
import com.livk.excel.annotation.ExcelImport;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
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
        return parameter.hasMethodAnnotation(ExcelImport.class) && parameter.hasParameterAnnotation(ExcelData.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (!Arrays.asList(parameter.getParameterType().getInterfaces()).contains(Collection.class)) {
            throw new IllegalArgumentException("Excel upload request resolver error, @ExcelData parameter is not Collection ");
        }
        var importExcel = parameter.getMethodAnnotation(ExcelImport.class);
        if (Objects.nonNull(importExcel)) {
            var listener = BeanUtils.instantiateClass(importExcel.parse());
            var request = webRequest.getNativeRequest(HttpServletRequest.class);
            Class<?> excelModelClass = ResolvableType.forMethodParameter(parameter).getGeneric(0).resolve();
            return listener.parse(getInputStream(request, importExcel.fileName()), excelModelClass).getCollectionData();
        }
        return null;
    }

    private InputStream getInputStream(HttpServletRequest request, String fileName) {
        try {
            if (request instanceof MultipartRequest) {
                MultipartFile file = ((MultipartRequest) request).getFile(fileName);
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
