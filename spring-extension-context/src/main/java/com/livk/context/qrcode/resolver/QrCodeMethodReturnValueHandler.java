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

import com.livk.commons.util.AnnotationUtils;
import com.livk.context.qrcode.PicType;
import com.livk.context.qrcode.QrCodeManager;
import com.livk.context.qrcode.annotation.ResponseQrCode;
import com.livk.context.qrcode.support.QrCodeSupport;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author livk
 */
public class QrCodeMethodReturnValueHandler extends QrCodeSupport implements AsyncHandlerMethodReturnValueHandler {

	public QrCodeMethodReturnValueHandler(QrCodeManager qrCodeManager) {
		super(qrCodeManager);
	}

	@Override
	public boolean supportsReturnType(@NonNull MethodParameter returnType) {
		return AnnotationUtils.hasAnnotationElement(returnType, ResponseQrCode.class);
	}

	@Override
	public void handleReturnValue(Object returnValue, @NonNull MethodParameter returnType,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException {
		mavContainer.setRequestHandled(true);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		Assert.notNull(response, "response not be null");
		AnnotationAttributes attributes = createAttributes(returnValue, returnType);
		PicType type = attributes.getEnum("type");
		BufferedImage bufferedImage = toBufferedImage(returnValue, attributes);
		try (ServletOutputStream outputStream = response.getOutputStream()) {
			setResponse(type, response);
			write(bufferedImage, type.name(), outputStream);
		}
	}

	private void setResponse(PicType type, HttpServletResponse response) {
		response.setContentType(type == PicType.JPG ? MediaType.IMAGE_JPEG_VALUE : MediaType.IMAGE_PNG_VALUE);
		response.setCharacterEncoding("UTF-8");
	}

	@Override
	public boolean isAsyncReturnValue(Object returnValue, @NonNull MethodParameter returnType) {
		return AnnotationUtils.hasAnnotationElement(returnType, ResponseQrCode.class);
	}

}
