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

import com.livk.autoconfigure.easyexcel.annotation.ExcelReturn;
import com.livk.autoconfigure.easyexcel.exception.ExcelExportException;
import com.livk.autoconfigure.easyexcel.utils.EasyExcelSupport;
import com.livk.commons.util.AnnotationUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.ErrorResponseException;
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
 */
public class ExcelMethodReturnValueHandler implements AsyncHandlerMethodReturnValueHandler {

	/**
	 * The constant UTF8.
	 */
	public static final String UTF8 = "UTF-8";

	@Override
	public boolean supportsReturnType(@NonNull MethodParameter returnType) {
		return AnnotationUtils.hasAnnotationElement(returnType, ExcelReturn.class);
	}

	@Override
	public void handleReturnValue(Object returnValue, @NonNull MethodParameter returnType, ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest) {
		mavContainer.setRequestHandled(true);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		ExcelReturn excelReturn = AnnotationUtils.getAnnotationElement(returnType, ExcelReturn.class);
		Assert.notNull(response, "response not be null");
		Assert.notNull(excelReturn, "excelReturn not be null");
		if (returnValue instanceof Collection) {
			Class<?> excelModelClass = ResolvableType.forMethodParameter(returnType).resolveGeneric(0);
			this.write(excelReturn, response, excelModelClass, Map.of("sheet", (Collection<?>) returnValue));
		} else if (returnValue instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Collection<?>> result = (Map<String, Collection<?>>) returnValue;
			Class<?> excelModelClass = ResolvableType.forMethodParameter(returnType).getGeneric(1).resolveGeneric(0);
			this.write(excelReturn, response, excelModelClass, result);
		} else {
			throw new ExcelExportException("the return class is not java.util.Collection or java.util.Map");
		}
	}

	/**
	 * Write.
	 *
	 * @param excelReturn     the excel return
	 * @param response        the response
	 * @param excelModelClass the excel model class
	 * @param result          the result
	 */
	private void write(ExcelReturn excelReturn, HttpServletResponse response, Class<?> excelModelClass, Map<String, Collection<?>> result) {
		this.setResponse(excelReturn, response);
		try (ServletOutputStream outputStream = response.getOutputStream()) {
			EasyExcelSupport.write(outputStream, excelModelClass, excelReturn.template(), result);
		} catch (IOException e) {
			throw new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
	}

	/**
	 * set response
	 *
	 * @param excelReturn the excel return
	 * @param response    response
	 */
	private void setResponse(ExcelReturn excelReturn, HttpServletResponse response) {
		String fileName = EasyExcelSupport.fileName(excelReturn);
		String contentType = MediaTypeFactory.getMediaType(fileName).map(MediaType::toString)
			.orElse("application/vnd.ms-excel");
		response.setContentType(contentType);
		response.setCharacterEncoding(UTF8);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
	}

	@Override
	public boolean isAsyncReturnValue(Object returnValue, @NonNull MethodParameter returnType) {
		return AnnotationUtils.hasAnnotationElement(returnType, ExcelReturn.class);
	}

}
