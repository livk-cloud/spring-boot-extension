/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.autoconfigure.qrcode.annotation;

import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.livk.autoconfigure.qrcode.enums.PicType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * QRCode
 * </p>
 *
 * @author livk
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseQRCode {

    /**
     * 二维码宽度
     *
     * @return the int
     */
    int width() default 400;

    /**
     * 二维码高度
     *
     * @return the int
     */
    int height() default 400;

    /**
     * On color int.
     *
     * @return the int
     * @see MatrixToImageConfig#getPixelOnColor()
     */
    int onColor() default MatrixToImageConfig.BLACK;

    /**
     * Off color int.
     *
     * @return the int
     * @see MatrixToImageConfig#getPixelOnColor()
     */
    int offColor() default MatrixToImageConfig.WHITE;

    /**
     * 图片类型
     *
     * @return the pic type
     */
    PicType type() default PicType.JPG;
}
