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

package com.livk.context.qrcode;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.livk.context.qrcode.support.GoogleQrCodeManager;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class QrCodeManagerTests {

	final JsonMapper mapper = JsonMapper.builder().build();

	final QrCodeManager manager = GoogleQrCodeManager.of(mapper);

	@Test
	void generateTextQRCode() throws IOException {
		QrCodeEntity<String> entity = new QrCodeEntity<>("hello", 400, 400, new MatrixToImageConfig(), PicType.PNG);
		BufferedImage image = manager.generate(entity);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		ImageIO.write(image, "png", outputStream);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

		String text = manager.parser(inputStream);

		assertThat(text).isEqualTo("hello");
	}

	@Test
	void generateJsonQRCode() throws IOException {
		Map<String, String> map = Map.of("username", "livk", "password", "123456");
		QrCodeEntity<Map<String, String>> entity = new QrCodeEntity<>(map, 400, 400, new MatrixToImageConfig(),
				PicType.PNG);
		BufferedImage image = manager.generate(entity);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		ImageIO.write(image, "png", outputStream);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

		String text = manager.parser(inputStream);

		assertThat(mapper.writeValueAsString(map)).isEqualTo(text);
	}

}
