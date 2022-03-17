package com.livk.excel.mvc.resolver;

import com.alibaba.excel.EasyExcel;
import com.livk.excel.annotation.ExcelReturn;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Collection;

/**
 * <p>
 * ExcelMethodResolver
 * </p>
 *
 * @author livk
 * @date 2022/2/11
 */
public class ExcelMethodReturnValueHandler implements AsyncHandlerMethodReturnValueHandler {

    public static final String UTF8 = "UTF-8";

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.hasMethodAnnotation(ExcelReturn.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        ExcelReturn excelReturn = returnType.getMethodAnnotation(ExcelReturn.class);
        Assert.notNull(response, "response not be null");
        Assert.notNull(excelReturn, "excelReturn not be null");
        if (returnValue instanceof Collection) {
            ServletOutputStream outputStream = response.getOutputStream();
            Class<?> excelModelClass = ResolvableType.forMethodParameter(returnType).resolveGeneric(0);
            String fileName = excelReturn.fileName().concat(excelReturn.suffix().getName());
            String contentType = MediaTypeFactory.getMediaType(fileName).map(MediaType::toString).orElse("application/vnd.ms-excel");
            response.setContentType(contentType);
            response.setCharacterEncoding(UTF8);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
            EasyExcel.write(outputStream, excelModelClass)
                    .sheet()
                    .doWrite((Collection<?>) returnValue);
            outputStream.close();
        }
    }

    @Override
    public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType) {
        return returnValue instanceof Collection && returnType.hasMethodAnnotation(ExcelReturn.class);
    }
}
