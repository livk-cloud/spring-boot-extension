package com.livk.autoconfigure.excel.annotation;

import com.livk.autoconfigure.excel.listener.DefaultExcelReadListener;
import com.livk.autoconfigure.excel.listener.ExcelReadListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>在Mvc的环境下支持{@link java.util.Collection}</p>
 * <p>{@example List<?>}</p>
 * <p></p><p>在Reactive的环境下支持
 * {@link java.util.Collection}、</p>
 * <p>{@link reactor.core.publisher.Mono}</p>
 * <p>{@example List<?>}</p>
 * <p>{@example Mono<List < ?>>}</p>
 *
 * @author livk
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelImport {

    Class<? extends ExcelReadListener<?>> parse() default DefaultExcelReadListener.class;

    String fileName() default "file";

    String paramName();

    boolean ignoreEmptyRow() default false;

}
