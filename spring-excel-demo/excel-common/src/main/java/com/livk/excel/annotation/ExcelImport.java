package com.livk.excel.annotation;

import com.livk.excel.listener.DefaultExcelReadListener;
import com.livk.excel.listener.ExcelReadListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * ImportExcel
 * </p>
 *
 * @author livk
 * @date 2022/1/19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelImport {

    Class<? extends ExcelReadListener<?>> parse() default DefaultExcelReadListener.class;

    String fileName() default "file";

    String paramName();

    boolean ignoreEmptyRow() default false;

}
