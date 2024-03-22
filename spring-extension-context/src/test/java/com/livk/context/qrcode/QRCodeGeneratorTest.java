/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.qrcode;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.livk.context.qrcode.support.GoogleQRCodeGenerator;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * QRCodeGeneratorTest
 * </p>
 *
 * @author livk
 */
class QRCodeGeneratorTest {

	JsonMapper mapper = JsonMapper.builder().build();

	QRCodeGenerator generator = new GoogleQRCodeGenerator(mapper);

	@Test
	void generateTextQRCode() throws IOException {
		com.livk.context.qrcode.QRCodeEntity<String> entity = new com.livk.context.qrcode.QRCodeEntity<>("hello", 400, 400, new MatrixToImageConfig(), com.livk.context.qrcode.PicType.PNG);
		BufferedImage image = generator.generateQRCode(entity);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		ImageIO.write(image, "png", outputStream);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

		String text = com.livk.context.qrcode.QRCodeUtils.parseQRCode(inputStream);

		assertEquals("hello", text);
	}

	@Test
	void generateJsonQRCode() throws IOException {
		Map<String, String> map = Map.of("username", "livk", "password", "123456");
		com.livk.context.qrcode.QRCodeEntity<Map<String, String>> entity = new QRCodeEntity<>(map, 400, 400, new MatrixToImageConfig(),
				PicType.PNG);
		BufferedImage image = generator.generateQRCode(entity);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		ImageIO.write(image, "png", outputStream);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

		String text = QRCodeUtils.parseQRCode(inputStream);

		assertEquals(mapper.writeValueAsString(map), text);
	}

}
