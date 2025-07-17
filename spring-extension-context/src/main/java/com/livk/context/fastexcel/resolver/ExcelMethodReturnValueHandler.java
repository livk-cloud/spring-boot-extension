/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.context.fastexcel.resolver;

import com.livk.commons.util.AnnotationUtils;
import com.livk.context.fastexcel.FastExcelSupport;
import com.livk.context.fastexcel.annotation.ResponseExcel;
import com.livk.context.fastexcel.exception.ExcelExportException;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.server.ServletServerHttpResponse;
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
 */
public class ExcelMethodReturnValueHandler implements AsyncHandlerMethodReturnValueHandler {

	/**
	 * The constant UTF8.
	 */
	public static final String UTF8 = "UTF-8";

	@Override
	public boolean supportsReturnType(@NonNull MethodParameter returnType) {
		return AnnotationUtils.hasAnnotationElement(returnType, ResponseExcel.class);
	}

	@Override
	public void handleReturnValue(Object returnValue, @NonNull MethodParameter returnType,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException {
		mavContainer.setRequestHandled(true);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		ResponseExcel responseExcel = AnnotationUtils.getAnnotationElement(returnType, ResponseExcel.class);
		Assert.notNull(response, "response not be null");
		Assert.notNull(responseExcel, "responseExcel not be null");
		if (this.canWrite(returnType.getParameterType())) {
			String fileName = ResponseExcel.Utils.parseName(responseExcel);
			String contentType = MediaTypeFactory.getMediaType(fileName)
				.map(MediaType::toString)
				.orElse("application/vnd.ms-excel");
			response.setCharacterEncoding(UTF8);
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
			try (ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response)) {
				if (returnValue instanceof Collection) {
					Class<?> excelModelClass = ResolvableType.forMethodParameter(returnType).resolveGeneric(0);
					FastExcelSupport.write(httpResponse.getBody(), excelModelClass, responseExcel.template(),
							Map.of("sheet", (Collection<?>) returnValue));
				}
				else if (returnValue instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<String, Collection<?>> result = (Map<String, Collection<?>>) returnValue;
					Class<?> excelModelClass = ResolvableType.forMethodParameter(returnType)
						.getGeneric(1)
						.resolveGeneric(0);
					FastExcelSupport.write(httpResponse.getBody(), excelModelClass, responseExcel.template(), result);
				}
			}
		}
		else {
			throw new ExcelExportException("the return class is not java.util.Collection or java.util.Map");
		}
	}

	@Override
	public boolean isAsyncReturnValue(Object returnValue, @NonNull MethodParameter returnType) {
		return AnnotationUtils.hasAnnotationElement(returnType, ResponseExcel.class);
	}

	private boolean canWrite(@NonNull Class<?> type) {
		return Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type);
	}

}
