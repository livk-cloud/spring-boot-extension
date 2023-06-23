/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.autoconfigure.qrcode.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.livk.autoconfigure.qrcode.entity.QRCodeEntity;
import com.livk.autoconfigure.qrcode.enums.PicType;
import com.livk.autoconfigure.qrcode.exception.QRCodeException;
import com.livk.commons.jackson.core.JacksonOperations;
import com.livk.commons.jackson.core.JacksonSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.awt.image.BufferedImage;

/**
 * The type Google qrcode generator.
 *
 * @author livk
 */
@Slf4j
public class GoogleQRCodeGenerator implements QRCodeGenerator {

	private final JacksonOperations jacksonOperations;

	/**
	 * Instantiates a new Google qrcode generator.
	 *
	 * @param builder the builder
	 */
	public GoogleQRCodeGenerator(Jackson2ObjectMapperBuilder builder) {
		ObjectMapper mapper = builder.build();
		jacksonOperations = JacksonSupport.create(mapper);
	}

	@Override
	public <T> BufferedImage generateQRCode(QRCodeEntity<T> entity) {
		return generateQRCode(jacksonOperations.writeValueAsString(entity.content()), entity.width(),
			entity.height(), entity.config(), entity.type());
	}

	@Override
	public BufferedImage generateQRCode(String content, int width, int height, MatrixToImageConfig config, PicType type) {
		try {
			QRCodeWriter writer = new QRCodeWriter();
			BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);
			return MatrixToImageWriter.toBufferedImage(matrix, config);
		} catch (WriterException e) {
			log.error("{}", e.getMessage(), e);
			throw new QRCodeException("生成二维码失败", e);
		}
	}
}
