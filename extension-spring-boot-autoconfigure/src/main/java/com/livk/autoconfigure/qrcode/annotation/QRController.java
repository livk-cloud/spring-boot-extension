package com.livk.autoconfigure.qrcode.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * QRController
 * </p>
 *
 * @author livk
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@QRCode
@Controller
public @interface QRController {

    /**
     * Value string.
     *
     * @return the string
     */
    @AliasFor(annotation = Controller.class)
    String value() default "";
}
