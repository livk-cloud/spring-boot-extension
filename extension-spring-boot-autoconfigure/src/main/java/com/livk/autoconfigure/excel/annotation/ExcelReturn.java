package com.livk.autoconfigure.excel.annotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>在Mvc的环境下支持{@link java.util.Collection}、</p>
 * <p>{@link java.util.Map}</p>
 * <p>{@example List<?>}</p>
 * <p>{@example Map<String, List < ?>>}</p>
 * <p></p>
 * <p>在Reactive的环境下支持{@link java.util.Collection}、</p>
 * <p>{@link java.util.Map}、</p>
 * <p>{@link reactor.core.publisher.Mono}、</p>
 * <p>{@link reactor.core.publisher.Flux} </p>
 * <p>{@example List<?>}</p>
 * <p>{@example Map<String, List < ?>>}</p>
 * <p>{@example Mono<List < ?>>}</p>
 * <p>{@example Mono<Map < String, List < ?>>>}</p>
 * <p>{@example Flux<?>}</p>
 *
 * @author livk
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelReturn {

    String fileName();

    Suffix suffix() default Suffix.XLS;

    @Getter
    @RequiredArgsConstructor
    enum Suffix {

        XLS(".xls"), XLSM(".xlsm");

        private final String name;

    }

}
