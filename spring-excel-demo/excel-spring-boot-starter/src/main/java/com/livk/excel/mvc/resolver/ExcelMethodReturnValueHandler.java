package com.livk.excel.mvc.resolver;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.livk.excel.annotation.ExcelReturn;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
            this.setResponse(excelReturn, response);
            EasyExcel.write(outputStream, excelModelClass)
                    .sheet()
                    .doWrite((Collection<?>) returnValue);
            outputStream.close();
        }
        if (returnValue instanceof Map) {
            ServletOutputStream outputStream = response.getOutputStream();
            @SuppressWarnings("unchecked")
            Map<String, List<?>> result = (Map<String, List<?>>) returnValue;
            Class<?> excelModelClass = ResolvableType.forMethodParameter(returnType).getGeneric(1).resolveGeneric(0);
            this.setResponse(excelReturn, response);
            ExcelWriter writer = EasyExcel.write(outputStream, excelModelClass).build();
            for (Map.Entry<String, List<?>> entry : result.entrySet()) {
                WriteSheet sheet = EasyExcel.writerSheet(entry.getKey()).build();
                writer.write(entry.getValue(), sheet);
            }
            writer.finish();
            outputStream.close();
        }
    }

    private void setResponse(ExcelReturn excelReturn, HttpServletResponse response) {
        String fileName = excelReturn.fileName().concat(excelReturn.suffix().getName());
        String contentType = MediaTypeFactory.getMediaType(fileName).map(MediaType::toString).orElse("application/vnd.ms-excel");
        response.setContentType(contentType);
        response.setCharacterEncoding(UTF8);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
    }

    @Override
    public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType) {
        return returnType.hasMethodAnnotation(ExcelReturn.class);
    }
}
