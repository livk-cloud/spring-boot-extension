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

package com.livk.context.qrcode.resolver;

import com.livk.commons.io.DataBufferUtils;
import com.livk.commons.util.AnnotationUtils;
import com.livk.context.qrcode.PicType;
import com.livk.context.qrcode.QrCodeEntity;
import com.livk.context.qrcode.QrCodeManager;
import com.livk.context.qrcode.annotation.ResponseQrCode;
import com.livk.context.qrcode.exception.QrCodeException;
import com.livk.context.qrcode.support.QrCodeSupport;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.HandlerResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author livk
 */
public class ReactiveQrCodeMethodReturnValueHandler extends QrCodeSupport implements HandlerResultHandler, Ordered {

	private final ReactiveAdapterRegistry adapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

	/**
	 * Instantiates a new Reactive qr code method return value handler.
	 * @param qrCodeManager the qr code generator
	 */
	public ReactiveQrCodeMethodReturnValueHandler(QrCodeManager qrCodeManager) {
		super(qrCodeManager);
	}

	@Override
	public boolean supports(@NonNull HandlerResult result) {
		return AnnotationUtils.hasAnnotationElement(result.getReturnTypeSource(), ResponseQrCode.class)
				|| result.getReturnType().isAssignableFrom(QrCodeEntity.class);
	}

	@NonNull
	@Override
	public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
		Object returnValue = result.getReturnValue();
		ServerHttpResponse response = exchange.getResponse();
		ResolvableType returnType = result.getReturnType();
		ReactiveAdapter adapter = adapterRegistry.getAdapter(returnType.resolve(), returnValue);
		if (adapter != null) {
			if (Mono.class.isAssignableFrom(returnType.toClass())) {
				Mono<?> mono = (Mono<?>) returnValue;
				Assert.notNull(mono, "mono not be null");
				return mono.flatMap(o -> write(o, result.getReturnTypeSource(), response));
			}
		}
		else {
			return write(returnValue, result.getReturnTypeSource(), response);
		}
		throw new QrCodeException("current type is not supported:" + returnType.toClass());
	}

	private Mono<Void> write(Object value, MethodParameter parameter, ServerHttpResponse response) {
		AnnotationAttributes attributes = createAttributes(value, parameter);
		PicType type = attributes.getEnum("type");
		setResponse(type, response);
		byte[] bytes = toByteArray(value, attributes);
		Flux<DataBuffer> bufferFlux = DataBufferUtils.transform(bytes);
		return response.writeWith(bufferFlux);
	}

	private void setResponse(PicType type, ServerHttpResponse response) {
		HttpHeaders headers = response.getHeaders();
		headers.setContentType(type == PicType.JPG ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG);
		headers.setAcceptCharset(List.of(StandardCharsets.UTF_8));
	}

	@Override
	public int getOrder() {
		// 提高优先级，不然会被@ResponseBody优先处理掉
		return Ordered.HIGHEST_PRECEDENCE;
	}

}
