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

package com.livk.context.qrcode.resolver;

import com.livk.context.qrcode.QrCodeManager;
import com.livk.context.qrcode.annotation.RequestQrCodeText;
import com.livk.context.qrcode.support.QrCodeSupport;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.RequestPartServletServerHttpRequest;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author livk
 */
public class QrCodeMethodArgumentResolver implements HandlerMethodArgumentResolver {

	private final QrCodeHttpMessageReader reader;

	public QrCodeMethodArgumentResolver(QrCodeManager qrCodeManager) {
		this.reader = new QrCodeHttpMessageReader(qrCodeManager);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestQrCodeText.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			@NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		RequestQrCodeText qrCodeText = parameter.getParameterAnnotation(RequestQrCodeText.class);
		if (Objects.nonNull(qrCodeText) && Objects.nonNull(request)) {
			if (reader.canRead(parameter.getParameterType(), MediaType.valueOf(request.getContentType()))) {
				return reader.read(parameter.getParameterType(),
						new RequestPartServletServerHttpRequest(request, qrCodeText.fileName()));
			}
		}
		throw new IllegalArgumentException(
				"QrCode upload request resolver error, @RequestQRCodeText parameter type error");
	}

	private static class QrCodeHttpMessageReader extends QrCodeSupport implements HttpMessageConverter<Object> {

		private QrCodeHttpMessageReader(QrCodeManager qrCodeManager) {
			super(qrCodeManager);
		}

		@Override
		public boolean canRead(@NonNull Class<?> clazz, MediaType mediaType) {
			return mediaType == null || mediaType.toString().startsWith(MediaType.MULTIPART_FORM_DATA_VALUE);
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
			return super.parser(inputMessage.getBody());
		}

		@Override
		public void write(@NonNull Object returnValue, MediaType contentType, @NonNull HttpOutputMessage outputMessage)
				throws HttpMessageNotWritableException {
			throw new UnsupportedOperationException();
		}

	}

}
