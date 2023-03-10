package com.livk.caffeine.annotation;

import com.livk.caffeine.enums.CacheType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * DoubleCache
 * </p>
 *
 * @author livk
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleCache {

    String cacheName();

    /**
     * 支持SPEL表达式
     */
    String key();

    long timeOut() default 120;

    CacheType type() default CacheType.FULL;

}
