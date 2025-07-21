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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.livk.commons.jackson.support.JacksonSupport;
import com.livk.context.qrcode.PicType;
import com.livk.context.qrcode.QrCodeManager;
import com.livk.context.qrcode.exception.QrCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The type Google qrcode generator.
 *
 * @author livk
 */
@Slf4j
@RequiredArgsConstructor
public class GoogleQrCodeManager extends AbstractQrCodeManager implements QrCodeManager {

	private final Function<Object, String> command;

	/**
	 * Instantiates a new Google qrcode generator.
	 * @param mapper the mapper
	 */
	public static GoogleQrCodeManager of(ObjectMapper mapper) {
		JacksonSupport support = new JacksonSupport(mapper);
		return new GoogleQrCodeManager(support::writeValueAsString);
	}

	@Override
	protected String convert(Object content) {
		return command.apply(content);
	}

	@Override
	public BufferedImage generate(String content, int width, int height, MatrixToImageConfig config, PicType type) {
		try {
			QRCodeWriter writer = new QRCodeWriter();
			BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);
			return MatrixToImageWriter.toBufferedImage(matrix, config);
		}
		catch (WriterException ex) {
			log.error("{}", ex.getMessage(), ex);
			throw new QrCodeException("Failed to generate a QRCode", ex);
		}
	}

	@Override
	public String parser(InputStream inputStream) {
		try {
			BufferedImage image = ImageIO.read(inputStream);
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Map<DecodeHintType, Object> hints = new HashMap<>();
			hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
			Result result = new MultiFormatReader().decode(binaryBitmap, hints);// 解码
			return result.getText();
		}
		catch (IOException | NotFoundException ex) {
			throw new QrCodeException(ex);
		}
	}

}
