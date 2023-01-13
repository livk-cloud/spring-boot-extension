package com.livk.autoconfigure.qrcode.annotation;

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
public @interface QRCode {

    /**
     * Width int.
     *
     * @return the int
     */
    int width() default 400;

    /**
     * Height int.
     *
     * @return the int
     */
    int height() default 400;

    /**
     * Type pic type.
     *
     * @return the pic type
     */
    PicType type() default PicType.JPG;
}
