package com.livk.autoconfigure.easyexcel.annotation;

import com.livk.autoconfigure.easyexcel.listener.DefaultExcelReadListener;
import com.livk.autoconfigure.easyexcel.listener.ExcelReadListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>在Mvc的环境下支持{@link java.util.Collection}</p>
 * <p>{example List}</p>
 * <p>在Reactive的环境下支持
 * {@link java.util.Collection}、</p>
 * <p>{@link reactor.core.publisher.Mono}</p>
 * <p>{example List}</p>
 * <p>{example Mono List }</p>
 *
 * @author livk
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelImport {

    /**
     * Parse class.
     *
     * @return the class
     */
    Class<? extends ExcelReadListener<?>> parse() default DefaultExcelReadListener.class;

    /**
     * File name string.
     *
     * @return the string
     */
    String fileName() default "file";

    /**
     * Param name string.
     *
     * @return the string
     */
    String paramName();

    /**
     * Ignore empty row boolean.
     *
     * @return the boolean
     */
    boolean ignoreEmptyRow() default false;

}
