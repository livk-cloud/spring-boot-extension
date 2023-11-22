/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.core.qrcode.resolver;

import com.livk.commons.util.AnnotationUtils;
import com.livk.core.qrcode.PicType;
import com.livk.core.qrcode.QRCodeGenerator;
import com.livk.core.qrcode.annotation.ResponseQRCode;
import com.livk.core.qrcode.support.QRCodeGeneratorSupport;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * <p>
 * QRCodeMethodReturnValueHandler
 * </p>
 *
 * @author livk
 */
public class QRCodeMethodReturnValueHandler extends QRCodeGeneratorSupport
		implements AsyncHandlerMethodReturnValueHandler {

	/**
	 * Instantiates a new Qr code method return value handler.
	 * @param qrCodeGenerator the qr code generator
	 */
	public QRCodeMethodReturnValueHandler(QRCodeGenerator qrCodeGenerator) {
		super(qrCodeGenerator);
	}

	@Override
	public boolean supportsReturnType(@NonNull MethodParameter returnType) {
		return AnnotationUtils.hasAnnotationElement(returnType, ResponseQRCode.class);
	}

	@Override
	public void handleReturnValue(Object returnValue, @NonNull MethodParameter returnType,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException {
		mavContainer.setRequestHandled(true);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		Assert.notNull(response, "response not be null");
		AnnotationAttributes attributes = createAttributes(returnValue, returnType, ResponseQRCode.class);
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
		return AnnotationUtils.hasAnnotationElement(returnType, ResponseQRCode.class);
	}

}
