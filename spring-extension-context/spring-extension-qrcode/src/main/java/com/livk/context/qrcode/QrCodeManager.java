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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * The interface Qr code generator.
 *
 * @author livk
 */
public interface QrCodeManager {

	/**
	 * 生成二维码
	 * @param entity 二维码实体
	 * @return bufferedImage
	 */
	BufferedImage generate(QrCodeEntity<?> entity);

	/**
	 * 解析二维码
	 * @param inputStream 输入流
	 * @return string
	 */
	String parser(InputStream inputStream);

	/**
	 * 解析二维码
	 * @param input 输入流
	 * @return string
	 */
	default String parser(byte[] input) {
		return parser(new ByteArrayInputStream(input));
	}

}
