package com.livk.excel.mvc.resolver;

import com.alibaba.excel.EasyExcel;
import com.livk.excel.annotation.ExcelReturn;
import com.livk.excel.exception.ExcelExportException;
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

import java.io.IOException;
import java.util.Collection;
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
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) {
		mavContainer.setRequestHandled(true);
		var response = webRequest.getNativeResponse(HttpServletResponse.class);
		var excelReturn = returnType.getMethodAnnotation(ExcelReturn.class);
		Assert.notNull(response, "response not be null");
		Assert.notNull(excelReturn, "excelReturn not be null");
		if (returnValue instanceof Collection) {
			var excelModelClass = ResolvableType.forMethodParameter(returnType).resolveGeneric(0);
			this.setResponse(excelReturn, response);
			this.write(response, excelModelClass, Map.of("sheet", (Collection<?>) returnValue));
		}
		else if (returnValue instanceof Map) {
			@SuppressWarnings("unchecked")
			var result = (Map<String, Collection<?>>) returnValue;
			var excelModelClass = ResolvableType.forMethodParameter(returnType).getGeneric(1).resolveGeneric(0);
			this.setResponse(excelReturn, response);
			this.write(response, excelModelClass, result);
		}
		else {
			throw new ExcelExportException("the return class is not java.util.Collection or java.util.Map");
		}
	}

	public void write(HttpServletResponse response, Class<?> excelModelClass, Map<String, Collection<?>> result) {
		try (var outputStream = response.getOutputStream()) {
			var writer = EasyExcel.write(outputStream, excelModelClass).build();
			for (var entry : result.entrySet()) {
				var sheet = EasyExcel.writerSheet(entry.getKey()).build();
				writer.write(entry.getValue(), sheet);
			}
			writer.finish();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setResponse(ExcelReturn excelReturn, HttpServletResponse response) {
		var fileName = excelReturn.fileName().concat(excelReturn.suffix().getName());
		var contentType = MediaTypeFactory.getMediaType(fileName).map(MediaType::toString)
				.orElse("application/vnd.ms-excel");
		response.setContentType(contentType);
		response.setCharacterEncoding(UTF8);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
	}

	@Override
	public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType) {
		return returnType.hasMethodAnnotation(ExcelReturn.class);
	}

}
