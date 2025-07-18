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

package com.livk.context.qrcode.resolver;

import com.livk.context.qrcode.QrCodeManager;
import com.livk.context.qrcode.annotation.RequestQrCodeText;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.RequestPartServletServerHttpRequest;

import java.util.Objects;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class QrCodeMethodArgumentResolver implements HandlerMethodArgumentResolver {

	private final QrCodeManager codeManager;

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
			if (this.canRead(request)) {
				HttpInputMessage part = new RequestPartServletServerHttpRequest(request, qrCodeText.fileName());
				return codeManager.parser(part.getBody());
			}
		}
		throw new IllegalArgumentException(
				"QrCode upload request resolver error, @RequestQRCodeText parameter type error");
	}

	private boolean canRead(HttpServletRequest request) {
		String contentType = request.getContentType();
		return contentType != null && contentType.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE);
	}

}
