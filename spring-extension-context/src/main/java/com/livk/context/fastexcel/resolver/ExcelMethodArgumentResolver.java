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

import com.livk.commons.util.BeanUtils;
import com.livk.context.fastexcel.ExcelDataType;
import com.livk.context.fastexcel.annotation.ExcelParam;
import com.livk.context.fastexcel.annotation.RequestExcel;
import com.livk.context.fastexcel.listener.ExcelMapReadListener;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.RequestPartServletServerHttpRequest;

import java.io.IOException;
import java.util.List;
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
			ExcelHttpMessageReader reader = new ExcelHttpMessageReader(listener, parameter, requestExcel);
			if (reader.canRead(parameter.getParameterType(), MediaType.valueOf(request.getContentType()))) {
				return reader.read(parameter.getParameterType(),
						new RequestPartServletServerHttpRequest(request, excelParam.fileName()));
			}
		}
		throw new IllegalArgumentException("Excel upload request resolver error, @ExcelData parameter type error");
	}

	@RequiredArgsConstructor
	private static class ExcelHttpMessageReader implements HttpMessageConverter<Object> {

		private final ExcelMapReadListener<?> listener;

		private final MethodParameter parameter;

		private final RequestExcel requestExcel;

		@Override
		public boolean canRead(@NonNull Class<?> clazz, MediaType mediaType) {
			try {
				ExcelDataType.match(clazz);
			}
			catch (Exception e) {
				return false;
			}
			return mediaType.toString().startsWith(MediaType.MULTIPART_FORM_DATA_VALUE);
		}

		@Override
		public boolean canWrite(@NonNull Class<?> type, MediaType mediaType) {
			throw new UnsupportedOperationException();
		}

		@NonNull
		@Override
		public List<MediaType> getSupportedMediaTypes() {
			return List.of();
		}

		@NonNull
		@Override
		public Object read(@NonNull Class<?> parameterType, HttpInputMessage inputMessage)
				throws IOException, HttpMessageNotReadableException {
			ExcelDataType dataType = ExcelDataType.match(parameterType);
			Class<?> excelModelClass = dataType.getFunction().apply(ResolvableType.forMethodParameter(parameter));
			listener.execute(inputMessage.getBody(), excelModelClass, requestExcel.ignoreEmptyRow());
			return listener.getData(dataType);
		}

		@Override
		public void write(@NonNull Object returnValue, MediaType contentType, @NonNull HttpOutputMessage outputMessage)
				throws HttpMessageNotWritableException {
			throw new UnsupportedOperationException();
		}

	}

}
