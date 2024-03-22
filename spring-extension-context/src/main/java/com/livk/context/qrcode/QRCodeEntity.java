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

package com.livk.context.qrcode;

import com.google.zxing.client.j2se.MatrixToImageConfig;
import java.awt.Color;

/**
 * The type Qr code entity.
 *
 * @param <T> the type parameter
 * @author livk
 */
public record QRCodeEntity<T>(T content, int width, int height, MatrixToImageConfig config, PicType type) {

	/**
	 * Builder qr code entity builder.
	 * @param <T> the type parameter
	 * @param content the content
	 * @return the qr code entity builder
	 */
	public static <T> QRCodeEntityBuilder<T> builder(T content) {
		return new QRCodeEntityBuilder<>(content);
	}

	/**
	 * The type Qr code entity builder.
	 *
	 * @param <T> the type parameter
	 */
	public static class QRCodeEntityBuilder<T> {

		private final T content;

		private Integer width = 400;

		private Integer height = 400;

		private int onColor = MatrixToImageConfig.BLACK;

		private int offColor = MatrixToImageConfig.WHITE;

		private PicType type = PicType.PNG;

		/**
		 * Instantiates a new Qr code entity builder.
		 * @param content the content
		 */
		QRCodeEntityBuilder(T content) {
			this.content = content;
		}

		/**
		 * Width qr code entity builder.
		 * @param width the width
		 * @return the qr code entity builder
		 */
		public QRCodeEntityBuilder<T> width(int width) {
			this.width = width;
			return this;
		}

		/**
		 * Height qr code entity builder.
		 * @param height the height
		 * @return the qr code entity builder
		 */
		public QRCodeEntityBuilder<T> height(int height) {
			this.height = height;
			return this;
		}

		/**
		 * On color qr code entity builder.
		 * @param onColor the on color
		 * @return the qr code entity builder
		 */
		public QRCodeEntityBuilder<T> onColor(int onColor) {
			this.onColor = onColor;
			return this;
		}

		/**
		 * On color qr code entity builder.
		 * @param color the color
		 * @return the qr code entity builder
		 */
		public QRCodeEntityBuilder<T> onColor(Color color) {
			this.onColor = color.getRGB();
			return this;
		}

		/**
		 * Off color qr code entity builder.
		 * @param offColor the off color
		 * @return the qr code entity builder
		 */
		public QRCodeEntityBuilder<T> offColor(int offColor) {
			this.offColor = offColor;
			return this;
		}

		/**
		 * Off color qr code entity builder.
		 * @param color the color
		 * @return the qr code entity builder
		 */
		public QRCodeEntityBuilder<T> offColor(Color color) {
			this.offColor = color.getRGB();
			return this;
		}

		/**
		 * Type qr code entity builder.
		 * @param type the type
		 * @return the qr code entity builder
		 */
		public QRCodeEntityBuilder<T> type(PicType type) {
			this.type = type;
			return this;
		}

		/**
		 * Build qr code entity.
		 * @return the qr code entity
		 */
		public QRCodeEntity<T> build() {
			MatrixToImageConfig imageConfig = new MatrixToImageConfig(this.onColor, this.offColor);
			return new QRCodeEntity<>(this.content, this.width, this.height, imageConfig, this.type);
		}

	}
}
