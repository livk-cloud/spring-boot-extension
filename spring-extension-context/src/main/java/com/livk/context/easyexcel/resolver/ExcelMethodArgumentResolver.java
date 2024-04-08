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

package com.livk.context.easyexcel.resolver;

import com.livk.commons.util.BeanUtils;
import com.livk.context.easyexcel.converter.ExcelHttpMessageConverter;
import com.livk.context.easyexcel.annotation.ExcelParam;
import com.livk.context.easyexcel.annotation.RequestExcel;
import com.livk.context.easyexcel.listener.ExcelMapReadListener;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
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
			ExcelHttpMessageConverter converter = ExcelHttpMessageConverter.readExcel(listener, parameter,
					requestExcel);
			if (converter.canRead(parameter.getParameterType(), MediaType.valueOf(request.getContentType()))) {
				return converter.read(parameter.getParameterType(),
						new RequestPartServletServerHttpRequest(request, excelParam.fileName()));
			}
		}
		throw new IllegalArgumentException("Excel upload request resolver error, @ExcelData parameter type error");
	}

}
