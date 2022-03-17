package com.livk.excel.annotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    String fileName();

    Suffix suffix() default Suffix.XLS;

    @Getter
    @RequiredArgsConstructor
    enum Suffix {
        XLS(".xls"),
        XLSM(".xlsm");

        private final String name;
    }
}
