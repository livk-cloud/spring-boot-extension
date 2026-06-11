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

package com.livk.context.qrcode.annotation;

import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.livk.context.qrcode.PicType;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class QrCodeAnnotationTests {

	@Test
	void qrCodeControllerComposesControllerAndResponseQrCode() {
		assertThat(QrCodeController.class).hasAnnotation(Controller.class);
		assertThat(QrCodeController.class).hasAnnotation(ResponseQrCode.class);
	}

	@Test
	void requestQrCodeTextDefaultFileName() throws Exception {
		assertThat(RequestQrCodeText.class.getDeclaredMethod("fileName").getDefaultValue()).isEqualTo("file");
	}

	@Test
	void responseQrCodeDefaults() throws Exception {
		assertThat(ResponseQrCode.class.getDeclaredMethod("width").getDefaultValue()).isEqualTo(400);
		assertThat(ResponseQrCode.class.getDeclaredMethod("height").getDefaultValue()).isEqualTo(400);
		assertThat(ResponseQrCode.class.getDeclaredMethod("onColor").getDefaultValue())
			.isEqualTo(MatrixToImageConfig.BLACK);
		assertThat(ResponseQrCode.class.getDeclaredMethod("offColor").getDefaultValue())
			.isEqualTo(MatrixToImageConfig.WHITE);
		assertThat(ResponseQrCode.class.getDeclaredMethod("type").getDefaultValue()).isEqualTo(PicType.JPG);
	}

}
