/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.fastexcel.resolver;

import com.livk.commons.io.DataBufferUtils;
import com.livk.commons.util.AnnotationUtils;
import com.livk.context.fastexcel.FastExcelSupport;
import com.livk.context.fastexcel.annotation.ResponseExcel;
import com.livk.context.fastexcel.exception.ExcelExportException;
import org.jspecify.annotations.NonNull;
import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.HandlerResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * <p>
 * ReactiveExcelMethodReturnValueHandler
 * </p>
 *
 * @author livk
 */
public class ReactiveExcelMethodReturnValueHandler implements HandlerResultHandler, Ordered {

	/**
	 * The constant EXCEL_MEDIA_TYPE.
	 */
	public static final MediaType EXCEL_MEDIA_TYPE = new MediaType("application", "vnd.ms-excel");

	private static final Function<Collection<?>, Map<String, Collection<?>>> defaultFunction = c -> Map.of("sheet", c);

	private final ReactiveAdapterRegistry adapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

	@Override
	public boolean supports(@NonNull HandlerResult result) {
		return AnnotationUtils.hasAnnotationElement(result.getReturnTypeSource(), ResponseExcel.class);
	}

	@SuppressWarnings("unchecked")
	@NonNull
	@Override
	public Mono<Void> handleResult(@NonNull ServerWebExchange exchange, HandlerResult result) {
		Object returnValue = result.getReturnValue();
		if (returnValue != null) {
			ResponseExcel responseExcel = AnnotationUtils.getAnnotationElement(result.getReturnTypeSource(),
					ResponseExcel.class);
			ServerHttpResponse response = exchange.getResponse();
			setResponse(responseExcel, response);
			ResolvableType returnType = result.getReturnType();
			ReactiveAdapter adapter = adapterRegistry.getAdapter(returnType.resolve(), returnValue);
			if (this.canWrite(returnType)) {
				Publisher<?> inputStream = adapter != null ? (Publisher<?>) returnValue : Mono.just(returnValue);
				if (inputStream instanceof Flux<?> flux) {
					Class<?> excelModelClass = returnType.resolveGeneric(0);
					Mono<Map<String, Collection<?>>> mono = flux.collectList().map(defaultFunction);
					return this.write(responseExcel, exchange.getResponse(), excelModelClass, mono);
				}
				else if (inputStream instanceof Mono<?>) {
					ResolvableType type = Mono.class.isAssignableFrom(returnType.toClass()) ? returnType.getGeneric(0)
							: returnType;
					if (Collection.class.isAssignableFrom(type.toClass())) {
						Class<?> excelModelClass = type.resolveGeneric(0);
						Mono<Map<String, Collection<?>>> mono = ((Mono<Collection<?>>) inputStream)
							.map(defaultFunction);
						return this.write(responseExcel, exchange.getResponse(), excelModelClass, mono);
					}
					else if (Map.class.isAssignableFrom(type.toClass())) {
						Class<?> excelModelClass = type.resolveGeneric(1);
						Mono<Map<String, Collection<?>>> mono = (Mono<Map<String, Collection<?>>>) inputStream;
						return this.write(responseExcel, exchange.getResponse(), excelModelClass, mono);
					}
				}
				throw new ExcelExportException("the return class is not java.util.Collection or java.util.Map");
			}
		}
		return Mono.empty();
	}

	private Mono<Void> write(ResponseExcel excelReturn, ReactiveHttpOutputMessage message, Class<?> excelModelClass,
			Mono<Map<String, Collection<?>>> result) {
		return result.flatMap(r -> {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			FastExcelSupport.write(outputStream, excelModelClass, excelReturn.template(), r);
			Flux<DataBuffer> bufferFlux = DataBufferUtils.transform(outputStream.toByteArray());
			return message.writeWith(bufferFlux);
		});
	}

	private void setResponse(ResponseExcel responseExcel, ServerHttpResponse response) {
		String fileName = ResponseExcel.Utils.parseName(responseExcel);
		MediaType mediaType = MediaTypeFactory.getMediaType(fileName).orElse(EXCEL_MEDIA_TYPE);
		HttpHeaders headers = response.getHeaders();
		headers.setContentType(mediaType);
		headers.setAcceptCharset(List.of(StandardCharsets.UTF_8));
		ContentDisposition contentDisposition = ContentDisposition.parse("attachment;filename=" + fileName);
		headers.setContentDisposition(contentDisposition);
	}

	@Override
	public int getOrder() {
		// 提高优先级，不然会被@ResponseBody优先处理掉
		return Ordered.HIGHEST_PRECEDENCE;
	}

	private boolean canWrite(ResolvableType elementType) {
		if (Flux.class.isAssignableFrom(elementType.toClass())) {
			return true;
		}

		Class<?> type;
		if (Mono.class.isAssignableFrom(elementType.toClass())) {
			type = elementType.resolveGeneric(0);
		}
		else {
			type = elementType.resolve();
		}
		return Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type);
	}

}
