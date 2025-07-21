/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.context.fastexcel.resolver;

import com.livk.commons.util.BeanUtils;
import com.livk.context.fastexcel.ExcelDataType;
import com.livk.context.fastexcel.annotation.ExcelParam;
import com.livk.context.fastexcel.annotation.RequestExcel;
import com.livk.context.fastexcel.listener.ExcelMapReadListener;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.RequestPartServletServerHttpRequest;

import java.util.Objects;

/**
 * <p>
 * ExcelMethodArgumentResolver
 * </p>
 *
 * @author livk
 */
public class ExcelMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasMethodAnnotation(RequestExcel.class) && parameter.hasParameterAnnotation(ExcelParam.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			@NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		RequestExcel requestExcel = parameter.getMethodAnnotation(RequestExcel.class);
		ExcelParam excelParam = parameter.getParameterAnnotation(ExcelParam.class);
		if (Objects.nonNull(requestExcel) && Objects.nonNull(request) && Objects.nonNull(excelParam)) {
			ExcelMapReadListener<?> listener = BeanUtils.instantiateClass(requestExcel.parse());
			if (this.canRead(parameter.getParameterType(), request)) {
				ExcelDataType dataType = ExcelDataType.match(parameter.getParameterType());
				Class<?> excelModelClass = dataType.getFunction().apply(ResolvableType.forMethodParameter(parameter));
				HttpInputMessage part = new RequestPartServletServerHttpRequest(request, excelParam.fileName());
				listener.execute(part.getBody(), excelModelClass, requestExcel.ignoreEmptyRow());
				return listener.getData(dataType);
			}
		}
		throw new IllegalArgumentException("Excel upload request resolver error, @ExcelParam parameter type error");
	}

	private boolean canRead(@NonNull Class<?> type, HttpServletRequest request) {
		try {
			ExcelDataType.match(type);
		}
		catch (Exception ex) {
			return false;
		}
		String contentType = request.getContentType();
		return contentType != null && contentType.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE);
	}

}
