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

import com.livk.commons.io.DataBufferUtils;
import com.livk.context.fastexcel.FastExcelSupport;
import com.livk.context.fastexcel.annotation.ResponseExcel;
import com.livk.context.fastexcel.exception.ExcelExportException;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageWriter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author livk
 */
public class ExcelHttpMessageWriter implements HttpMessageWriter<Object> {

	private static final Function<Collection<?>, Map<String, Collection<?>>> defaultFunction = c -> Map.of("sheet", c);

	@Override
	public List<MediaType> getWritableMediaTypes() {
		return List.of();
	}

	@Override
	public boolean canWrite(ResolvableType elementType, MediaType mediaType) {
		if (Flux.class.isAssignableFrom(elementType.toClass())) {
			return true;
		}
		else if (Mono.class.isAssignableFrom(elementType.toClass())) {
			Class<?> type = elementType.resolveGeneric(0);
			return canWrite(type);
		}
		else {
			return canWrite(elementType.toClass());
		}
	}

	private boolean canWrite(Class<?> type) {
		return Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Mono<Void> write(Publisher<?> inputStream, ResolvableType elementType, MediaType mediaType,
			ReactiveHttpOutputMessage message, Map<String, Object> hints) {
		ResponseExcel responseExcel = (ResponseExcel) hints.get("responseExcel");
		if (inputStream instanceof Flux<?> flux) {
			Class<?> excelModelClass = elementType.resolveGeneric(0);
			Mono<Map<String, Collection<?>>> mono = flux.collectList().map(defaultFunction);
			return this.write(responseExcel, message, excelModelClass, mono);
		}
		else if (inputStream instanceof Mono<?>) {
			ResolvableType type = Mono.class.isAssignableFrom(elementType.toClass()) ? elementType.getGeneric(0)
					: elementType;
			if (Collection.class.isAssignableFrom(type.toClass())) {
				Class<?> excelModelClass = type.resolveGeneric(0);
				Mono<Map<String, Collection<?>>> mono = ((Mono<Collection<?>>) inputStream).map(defaultFunction);
				return this.write(responseExcel, message, excelModelClass, mono);
			}
			else if (Map.class.isAssignableFrom(type.toClass())) {
				Class<?> excelModelClass = type.resolveGeneric(1);
				Mono<Map<String, Collection<?>>> mono = (Mono<Map<String, Collection<?>>>) inputStream;
				return this.write(responseExcel, message, excelModelClass, mono);
			}
		}
		throw new ExcelExportException("the return class is not java.util.Collection or java.util.Map");
	}

	private Mono<Void> write(ResponseExcel excelReturn, ReactiveHttpOutputMessage message, Class<?> excelModelClass,
			Mono<Map<String, Collection<?>>> result) {
		return result.flatMap(r -> this.write(excelReturn, message, excelModelClass, r));
	}

	private Mono<Void> write(ResponseExcel excelReturn, ReactiveHttpOutputMessage message, Class<?> excelModelClass,
			Map<String, Collection<?>> result) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		FastExcelSupport.write(outputStream, excelModelClass, excelReturn.template(), result);
		Flux<DataBuffer> bufferFlux = DataBufferUtils.transform(outputStream.toByteArray());
		return message.writeWith(bufferFlux);
	}

}
