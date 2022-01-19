package com.livk.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * ExcelReturn
 * </p>
 *
 * @author livk
 * @date 2022/1/19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelReturn {

    Class<?> packageClass();

    String dataName();
}
