/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.core.qrcode;

import com.google.zxing.client.j2se.MatrixToImageConfig;

import java.awt.image.BufferedImage;

/**
 * The interface Qr code generator.
 *
 * @author livk
 */
public interface QRCodeGenerator {
	/**
	 * 生成二维码
	 *
	 * @param entity 二维码实体
	 * @return BufferedImage
	 */
	BufferedImage generateQRCode(QRCodeEntity<?> entity);

	/**
	 * 生成二维码
	 *
	 * @param content 二维码内容
	 * @param width   二维码宽度
	 * @param height  二维码高度
	 * @param type    图片类型
	 * @return BufferedImage
	 */
	default BufferedImage generateQRCode(String content, int width, int height, PicType type) {
		return generateQRCode(content, width, height, new MatrixToImageConfig(), type);
	}

	/**
	 * 生成二维码
	 *
	 * @param content 二维码内容
	 * @param width   二维码宽度
	 * @param height  二维码高度
	 * @param config  二维码颜色配置
	 * @param type    图片类型
	 * @return BufferedImage
	 */
	BufferedImage generateQRCode(String content, int width, int height, MatrixToImageConfig config, PicType type);
}
