package com.livk.core.qrcode;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.livk.core.qrcode.support.GoogleQRCodeGenerator;
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
		QRCodeEntity<String> entity = new QRCodeEntity<>("hello", 400, 400, new MatrixToImageConfig(), PicType.PNG);
		BufferedImage image = generator.generateQRCode(entity);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		ImageIO.write(image, "png", outputStream);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

		String text = QRCodeUtils.parseQRCode(inputStream);

		assertEquals("hello", text);
	}

	@Test
	void generateJsonQRCode() throws IOException {
		Map<String, String> map = Map.of("username", "livk", "password", "123456");
		QRCodeEntity<Map<String, String>> entity = new QRCodeEntity<>(map, 400, 400, new MatrixToImageConfig(),
				PicType.PNG);
		BufferedImage image = generator.generateQRCode(entity);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		ImageIO.write(image, "png", outputStream);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

		String text = QRCodeUtils.parseQRCode(inputStream);

		assertEquals(mapper.writeValueAsString(map), text);
	}

}
