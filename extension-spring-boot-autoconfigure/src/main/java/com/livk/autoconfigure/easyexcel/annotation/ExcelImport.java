package com.livk.autoconfigure.easyexcel.annotation;

import com.livk.autoconfigure.easyexcel.listener.DefaultExcelMapReadListener;
import com.livk.autoconfigure.easyexcel.listener.ExcelMapReadListener;

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
    Class<? extends ExcelMapReadListener<?>> parse() default DefaultExcelMapReadListener.class;

    /**
     * Ignore empty row boolean.
     *
     * @return the boolean
     */
    boolean ignoreEmptyRow() default false;

}
