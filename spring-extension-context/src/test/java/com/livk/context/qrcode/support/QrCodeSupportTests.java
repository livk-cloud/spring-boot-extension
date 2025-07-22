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

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.livk.context.qrcode.QrCodeEntity;
import com.livk.context.qrcode.annotation.ResponseQrCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationAttributes;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class QrCodeSupportTests {

	static Support support = new Support();

	static QrCodeEntity<String> returnValue = QrCodeEntity.builder("QrCode").build();

	static MethodParameter parameter;

	static AnnotationAttributes attributes;

	@BeforeAll
	static void init() throws NoSuchMethodException {
		MethodParameter parameter = MethodParameter.forExecutable(Support.class.getDeclaredMethod("textCode"), -1);
		attributes = support.createAttributes(returnValue, parameter);
	}

	@Test
	void createAttributes() {
		AnnotationAttributes attributes = support.createAttributes(returnValue, parameter);
		assertEquals(returnValue.width(), attributes.getNumber("width").intValue());
		assertEquals(returnValue.height(), attributes.getNumber("height").intValue());
		assertEquals(returnValue.type(), attributes.getEnum("type"));
	}

	@Test
	void toBufferedImage() {
		BufferedImage bufferedImage = support.toBufferedImage(returnValue, attributes);
		assertNotNull(bufferedImage);
	}

	@Test
	void toByteArray() {
		byte[] bytes = support.toByteArray(returnValue, attributes);
		assertNotNull(bytes);
		assertTrue(bytes.length > 0);
	}

	@Test
	void write() {
		BufferedImage bufferedImage = support.toBufferedImage(returnValue, attributes);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		support.write(bufferedImage, "PNG", stream);
		byte[] bytes = stream.toByteArray();
		assertNotNull(bytes);
		assertTrue(bytes.length > 0);
	}

	static class Support extends QrCodeSupport {

		Support() {
			super(GoogleQrCodeManager.of(JsonMapper.builder().build()));
		}

		@ResponseQrCode
		public QrCodeEntity<String> textCode() {
			return QrCodeEntity.builder("QrCode").build();
		}

	}

}
