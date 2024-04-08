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

package com.livk.context.easyexcel.converter;

import com.livk.commons.io.DataBufferUtils;
import com.livk.context.easyexcel.EasyExcelSupport;
import com.livk.context.easyexcel.ExcelDataType;
import com.livk.context.easyexcel.annotation.RequestExcel;
import com.livk.context.easyexcel.listener.ExcelMapReadListener;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author livk
 */
public class ExcelHttpMessageReader implements HttpMessageReader<Object> {

	@NonNull
	@Override
	public List<MediaType> getReadableMediaTypes() {
		return List.of(MediaType.MULTIPART_FORM_DATA);
	}

	@Override
	public boolean canRead(@NonNull ResolvableType elementType, MediaType mediaType) {
		try {
			ResolvableType type = elementType;
			if (Objects.equals(type.resolve(), Mono.class)) {
				type = elementType.getGeneric(0);
			}
			if (type.getRawClass() == null) {
				return false;
			}
			if (Flux.class.isAssignableFrom(type.getRawClass())) {
				return true;
			}
			ExcelDataType.match(type.getRawClass());
		}
		catch (Exception e) {
			return false;
		}
		return mediaType.toString().startsWith(MediaType.MULTIPART_FORM_DATA_VALUE);
	}

	@NonNull
	@Override
	public Flux<Object> read(@NonNull ResolvableType elementType, @NonNull ReactiveHttpInputMessage message,
			@NonNull Map<String, Object> hints) {
		throw new UnsupportedOperationException("");
	}

	@NonNull
	@Override
	public Mono<Object> readMono(@NonNull ResolvableType elementType, @NonNull ReactiveHttpInputMessage message,
			@NonNull Map<String, Object> hints) {
		ExcelMapReadListener<?> listener = (ExcelMapReadListener<?>) hints.get("listener");
		RequestExcel requestExcel = (RequestExcel) hints.get("requestExcel");
		if (Objects.equals(elementType.resolve(), Mono.class)) {
			elementType = elementType.getGeneric(0);
		}
		if (elementType.getRawClass() != null) {

			ExcelDataType dataType = Flux.class.isAssignableFrom(elementType.getRawClass()) ? ExcelDataType.COLLECTION
					: ExcelDataType.match(elementType.getRawClass());
			Class<?> excelModelClass = dataType.getFunction().apply(elementType);
			return Mono.just(message.getBody())
				.flatMap(DataBufferUtils::transform)
				.doOnSuccess(in -> EasyExcelSupport.read(in, excelModelClass, listener, requestExcel.ignoreEmptyRow()))
				.map(in -> listener.getData(dataType));
		}
		return Mono.empty();
	}

}
