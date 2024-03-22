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

import com.livk.commons.io.DataBufferUtils;
import com.livk.commons.util.AnnotationUtils;
import com.livk.context.easyexcel.EasyExcelSupport;
import com.livk.context.easyexcel.annotation.ResponseExcel;
import com.livk.context.easyexcel.exception.ExcelExportException;
import org.springframework.core.Ordered;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
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

	@NonNull
	@SuppressWarnings("unchecked")
	@Override
	public Mono<Void> handleResult(@NonNull ServerWebExchange exchange, HandlerResult result) {
		Object returnValue = result.getReturnValue();
		if (returnValue == null) {
			return Mono.empty();
		}
		ResponseExcel excelReturn = AnnotationUtils.getAnnotationElement(result.getReturnTypeSource(),
				ResponseExcel.class);
		ServerHttpResponse response = exchange.getResponse();
		ResolvableType returnType = result.getReturnType();
		ReactiveAdapter adapter = adapterRegistry.getAdapter(returnType.resolve(), returnValue);
		if (adapter != null) {
			ResolvableType genericType = returnType.getGeneric();
			if (Flux.class.isAssignableFrom(returnType.toClass())) {
				Class<?> excelModelClass = genericType.toClass();
				Flux<?> flux = (Flux<?>) returnValue;
				Mono<Map<String, Collection<?>>> mono = flux.collectList().map(defaultFunction);
				return this.write(excelReturn, response, excelModelClass, mono);
			}
			else if (Mono.class.isAssignableFrom(returnType.toClass())) {
				if (Collection.class.isAssignableFrom(genericType.toClass())) {
					Class<?> excelModelClass = genericType.resolveGeneric(0);
					Mono<Map<String, Collection<?>>> mono = ((Mono<Collection<?>>) returnValue).map(defaultFunction);
					return this.write(excelReturn, response, excelModelClass, mono);
				}
				else if (Map.class.isAssignableFrom(genericType.toClass())) {
					Class<?> excelModelClass = genericType.getGeneric(1).resolveGeneric(0);
					Mono<Map<String, Collection<?>>> mono = (Mono<Map<String, Collection<?>>>) returnValue;
					return this.write(excelReturn, response, excelModelClass, mono);
				}
				else {
					throw new ExcelExportException("the return class is not java.util.Collection or java.util.Map");
				}
			}
			else {
				throw new ExcelExportException(
						"the return class is not reactor.core.publisher.Flux or reactor.core.publisher.Mono");
			}
		}
		else {
			if (Collection.class.isAssignableFrom(returnType.toClass())) {
				Class<?> excelModelClass = returnType.resolveGeneric(0);
				return this.write(excelReturn, response, excelModelClass,
						defaultFunction.apply((Collection<?>) returnValue));
			}
			else if (Map.class.isAssignableFrom(returnType.toClass())) {
				Map<String, Collection<?>> map = (Map<String, Collection<?>>) returnValue;
				Class<?> excelModelClass = returnType.getGeneric(1).resolveGeneric(0);
				return this.write(excelReturn, response, excelModelClass, map);
			}
			else {
				throw new ExcelExportException("the return class is not java.util.Collection or java.util.Map");
			}
		}
	}

	/**
	 * Write mono.
	 * @param excelReturn the excel return
	 * @param response the response
	 * @param excelModelClass the excel model class
	 * @param result the result
	 * @return the mono
	 */
	private Mono<Void> write(ResponseExcel excelReturn, ServerHttpResponse response, Class<?> excelModelClass,
			Mono<Map<String, Collection<?>>> result) {
		return result.flatMap(r -> this.write(excelReturn, response, excelModelClass, r));
	}

	/**
	 * Write mono.
	 * @param excelReturn the excel return
	 * @param response the response
	 * @param excelModelClass the excel model class
	 * @param result the result
	 * @return the mono
	 */
	private Mono<Void> write(ResponseExcel excelReturn, ServerHttpResponse response, Class<?> excelModelClass,
			Map<String, Collection<?>> result) {
		this.setResponse(excelReturn, response);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		EasyExcelSupport.write(outputStream, excelModelClass, excelReturn.template(), result);
		Flux<DataBuffer> bufferFlux = DataBufferUtils.transform(outputStream.toByteArray());
		return response.writeWith(bufferFlux);
	}

	private void setResponse(ResponseExcel excelReturn, ServerHttpResponse response) {
		String fileName = EasyExcelSupport.fileName(excelReturn);
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

}
