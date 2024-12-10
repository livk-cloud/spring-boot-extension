/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.fastexcel.converter;

import com.livk.context.fastexcel.FastExcelSupport;
import com.livk.context.fastexcel.ExcelDataType;
import com.livk.context.fastexcel.annotation.RequestExcel;
import com.livk.context.fastexcel.annotation.ResponseExcel;
import com.livk.context.fastexcel.listener.ExcelMapReadListener;
import lombok.AccessLevel;
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

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author livk
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelHttpMessageConverter implements HttpMessageConverter<Object> {

	private final ExcelMapReadListener<?> listener;

	private final MethodParameter parameter;

	private final RequestExcel requestExcel;

	private final ResponseExcel responseExcel;

	public static ExcelHttpMessageConverter readExcel(ExcelMapReadListener<?> listener, MethodParameter parameter,
			RequestExcel requestExcel) {
		return new ExcelHttpMessageConverter(listener, parameter, requestExcel, null);
	}

	public static ExcelHttpMessageConverter writeExcel(MethodParameter parameter, ResponseExcel responseExcel) {
		return new ExcelHttpMessageConverter(null, parameter, null, responseExcel);
	}

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
		return Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type);
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
		FastExcelSupport.read(inputMessage.getBody(), excelModelClass, listener, requestExcel.ignoreEmptyRow());
		return listener.getData(dataType);
	}

	@Override
	public void write(@NonNull Object returnValue, MediaType contentType, @NonNull HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		if (returnValue instanceof Collection) {
			Class<?> excelModelClass = ResolvableType.forMethodParameter(parameter).resolveGeneric(0);
			FastExcelSupport.write(outputMessage.getBody(), excelModelClass, responseExcel.template(),
					Map.of("sheet", (Collection<?>) returnValue));
		}
		else if (returnValue instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Collection<?>> result = (Map<String, Collection<?>>) returnValue;
			Class<?> excelModelClass = ResolvableType.forMethodParameter(parameter).getGeneric(1).resolveGeneric(0);
			FastExcelSupport.write(outputMessage.getBody(), excelModelClass, responseExcel.template(), result);
		}
	}

}
