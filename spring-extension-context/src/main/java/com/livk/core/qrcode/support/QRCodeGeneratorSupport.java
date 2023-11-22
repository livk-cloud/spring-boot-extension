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

package com.livk.core.qrcode.support;

import com.livk.commons.util.AnnotationUtils;
import com.livk.commons.util.BeanUtils;
import com.livk.core.qrcode.QRCodeEntity;
import com.livk.core.qrcode.QRCodeGenerator;
import com.livk.core.qrcode.exception.QRCodeException;
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
 * The type Qr code generator support.
 *
 * @author livk
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class QRCodeGeneratorSupport {

	private final QRCodeGenerator qrCodeGenerator;

	/**
	 * Create attributes annotation attributes.
	 * @param returnValue the return value
	 * @param parameter the parameter
	 * @param annotationClass the annotation class
	 * @return the annotation attributes
	 */
	protected AnnotationAttributes createAttributes(Object returnValue, MethodParameter parameter,
			Class<? extends Annotation> annotationClass) {
		if (returnValue instanceof QRCodeEntity<?> entity) {
			Map<String, Object> map = BeanUtils.convert(entity);
			return AnnotationAttributes.fromMap(map);
		}
		else {
			Annotation annotation = AnnotationUtils.getAnnotationElement(parameter, annotationClass);
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
		if (returnValue instanceof QRCodeEntity<?> entity) {
			return qrCodeGenerator.generateQRCode(entity);
		}
		else {
			QRCodeEntity<?> entity = QRCodeEntity.builder(returnValue)
				.height(attributes.getNumber("width").intValue())
				.width(attributes.getNumber("height").intValue())
				.onColor(attributes.getNumber("onColor").intValue())
				.offColor(attributes.getNumber("offColor").intValue())
				.type(attributes.getEnum("type"))
				.build();
			return qrCodeGenerator.generateQRCode(entity);
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
		catch (IOException e) {
			throw new QRCodeException("二维码写入失败", e);
		}
	}

}
