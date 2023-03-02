package com.livk.autoconfigure.easyexcel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Excel param.
 *
 * @author livk
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelParam {

    /**
     * Value string.
     *
     * @return the string
     */
    String fileName() default "file";
}
