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

package com.livk.context.qrcode.support;

import com.livk.commons.util.AnnotationUtils;
import com.livk.commons.util.BeanUtils;
import com.livk.context.qrcode.QrCodeEntity;
import com.livk.context.qrcode.QrCodeManager;
import com.livk.context.qrcode.annotation.ResponseQrCode;
import com.livk.context.qrcode.exception.QrCodeException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationAttributes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * The type Qr code support.
 *
 * @author livk
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class QrCodeSupport {

	private final QrCodeManager qrCodeManager;

	/**
	 * Create attributes annotation attributes.
	 * @param returnValue the return value
	 * @param parameter the parameter
	 * @return the annotation attributes
	 */
	protected AnnotationAttributes createAttributes(Object returnValue, MethodParameter parameter) {
		if (returnValue instanceof QrCodeEntity<?> entity) {
			Map<String, Object> map = BeanUtils.convert(entity);
			return AnnotationAttributes.fromMap(map);
		}
		else {
			Annotation annotation = AnnotationUtils.getAnnotationElement(parameter, ResponseQrCode.class);
			return AnnotationUtils.getAnnotationAttributes(parameter.getMethod(), annotation);
		}
	}

	/**
	 * To buffered image.
	 * @param returnValue the return value
	 * @param attributes the attributes
	 * @return the buffered image
	 */
	protected BufferedImage toBufferedImage(Object returnValue, AnnotationAttributes attributes) {
		if (returnValue instanceof QrCodeEntity<?> entity) {
			return qrCodeManager.generate(entity);
		}
		else {
			QrCodeEntity<?> entity = QrCodeEntity.builder(returnValue)
				.height(attributes.getNumber("width").intValue())
				.width(attributes.getNumber("height").intValue())
				.onColor(attributes.getNumber("onColor").intValue())
				.offColor(attributes.getNumber("offColor").intValue())
				.type(attributes.getEnum("type"))
				.build();
			return qrCodeManager.generate(entity);
		}
	}

	/**
	 * To byte array.
	 * @param returnValue the return value
	 * @param attributes the attributes
	 * @return the byte [ ]
	 */
	protected byte[] toByteArray(Object returnValue, AnnotationAttributes attributes) {
		BufferedImage bufferedImage = toBufferedImage(returnValue, attributes);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		String formatName = attributes.getEnum("type").name();
		this.write(bufferedImage, formatName, stream);
		return stream.toByteArray();
	}

	/**
	 * Write.
	 * @param bufferedImage the buffered image
	 * @param formatName the format name
	 * @param stream the stream
	 */
	protected void write(BufferedImage bufferedImage, String formatName, OutputStream stream) {
		try {
			ImageIO.write(bufferedImage, formatName, stream);
		}
		catch (IOException ex) {
			throw new QrCodeException("The QRCode was written to failure", ex);
		}
	}

}
