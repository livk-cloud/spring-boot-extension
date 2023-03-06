package com.livk.autoconfigure.easyexcel.annotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>在Mvc的环境下支持{@link java.util.Collection}、</p>
 * <p>{@link java.util.Map}</p>
 * <p>{example List}</p>
 * <p>{example Map String, List }</p>
 * <p>在Reactive的环境下支持{@link java.util.Collection}、</p>
 * <p>{@link java.util.Map}、</p>
 * <p>{@link reactor.core.publisher.Mono}、</p>
 * <p>{@link reactor.core.publisher.Flux} </p>
 * <p>{example List}</p>
 * <p>{example Map String, List }</p>
 * <p>{example Mono List }</p>
 * <p>{example Mono Map String, List}</p>
 * <p>{example Flux }</p>
 *
 * @author livk
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelReturn {

    /**
     * File name string.
     *
     * @return the string
     */
    String fileName();

    /**
     * Template path string.
     *
     * @return the string
     */
    String template() default "";

    /**
     * Suffix suffix.
     *
     * @return the suffix
     */
    Suffix suffix() default Suffix.XLSM;

    /**
     * The enum Suffix.
     */
    @Getter
    @RequiredArgsConstructor
    enum Suffix {

        /**
         * Xls suffix.
         */
        XLS(".xls"),

        /**
         * Xlsx suffix.
         */
        XLSX(".xlsx"),
        /**
         * Xlsm suffix.
         */
        XLSM(".xlsm");

        private final String name;

    }

}
