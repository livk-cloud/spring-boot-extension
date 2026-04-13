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

import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class QrCodeEntityTests {

	@Test
	void builderWithDefaults() {
		QrCodeEntity<String> entity = QrCodeEntity.builder("hello").build();
		assertThat(entity.content()).isEqualTo("hello");
		assertThat(entity.width()).isEqualTo(400);
		assertThat(entity.height()).isEqualTo(400);
		assertThat(entity.type()).isEqualTo(PicType.PNG);
		assertThat(entity.config()).isNotNull();
	}

	@Test
	void builderWithCustomDimensions() {
		QrCodeEntity<String> entity = QrCodeEntity.builder("hello").width(200).height(300).build();
		assertThat(entity.width()).isEqualTo(200);
		assertThat(entity.height()).isEqualTo(300);
	}

	@Test
	void builderWithCustomType() {
		QrCodeEntity<String> entity = QrCodeEntity.builder("hello").type(PicType.JPG).build();
		assertThat(entity.type()).isEqualTo(PicType.JPG);
	}

	@Test
	void builderWithColorInts() {
		QrCodeEntity<String> entity = QrCodeEntity.builder("hello")
			.onColor(Color.RED.getRGB())
			.offColor(Color.BLUE.getRGB())
			.build();
		assertThat(entity.config()).isNotNull();
	}

	@Test
	void builderWithColorObjects() {
		QrCodeEntity<String> entity = QrCodeEntity.builder("hello").onColor(Color.RED).offColor(Color.BLUE).build();
		assertThat(entity.config()).isNotNull();
	}

	@Test
	void builderWithNonStringContent() {
		QrCodeEntity<Integer> entity = QrCodeEntity.builder(42).build();
		assertThat(entity.content()).isEqualTo(42);
	}

}
