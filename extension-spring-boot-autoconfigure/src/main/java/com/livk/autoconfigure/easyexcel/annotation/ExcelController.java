package com.livk.autoconfigure.easyexcel.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * ExcelController
 * </p>
 *
 * @author livk
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Controller
@ExcelReturn(fileName = "out")
public @interface ExcelController {

    /**
     * Value string.
     *
     * @return the string
     */
    @AliasFor(annotation = Controller.class)
    String value() default "";
}
