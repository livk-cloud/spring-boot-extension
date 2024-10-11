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

package com.livk.context.qrcode.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.livk.commons.jackson.core.JacksonOps;
import com.livk.commons.jackson.core.JacksonSupport;
import com.livk.context.qrcode.PicType;
import com.livk.context.qrcode.QRCodeGenerator;
import com.livk.context.qrcode.exception.QRCodeException;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;

/**
 * The type Google qrcode generator.
 *
 * @author livk
 */
@Slf4j
public class GoogleQRCodeGenerator extends AbstractQRCodeGenerator implements QRCodeGenerator {

	private final JacksonOps jacksonOps;

	/**
	 * Instantiates a new Google qrcode generator.
	 * @param mapper the mapper
	 */
	public GoogleQRCodeGenerator(ObjectMapper mapper) {
		jacksonOps = new JacksonSupport(mapper);
	}

	@Override
	protected String convert(Object content) {
		return jacksonOps.writeValueAsString(content);
	}

	@Override
	public BufferedImage generate(String content, int width, int height, MatrixToImageConfig config, PicType type) {
		try {
			QRCodeWriter writer = new QRCodeWriter();
			BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);
			return MatrixToImageWriter.toBufferedImage(matrix, config);
		}
		catch (WriterException e) {
			log.error("{}", e.getMessage(), e);
			throw new QRCodeException("生成二维码失败", e);
		}
	}

}
