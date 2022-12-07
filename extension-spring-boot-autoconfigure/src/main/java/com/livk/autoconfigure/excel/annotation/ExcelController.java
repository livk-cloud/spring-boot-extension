package com.livk.autoconfigure.excel.annotation;

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
 * @date 2022/12/7
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Controller
@ExcelReturn(fileName = "out")
public @interface ExcelController {

    @AliasFor(annotation = Controller.class)
    String value() default "";
}
