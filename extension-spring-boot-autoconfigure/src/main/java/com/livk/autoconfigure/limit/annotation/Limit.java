package com.livk.autoconfigure.limit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * The interface Limit.
 *
 * @author livk
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {
    /**
     * 资源的key
     *
     * @return String string
     */
    String key() default "";

    /**
     * 单位时间
     *
     * @return int int
     */
    int rateInterval();

    /**
     * 单位(默认秒)
     *
     * @return TimeUnit time unit
     */
    TimeUnit rateIntervalUnit() default TimeUnit.SECONDS;

    /**
     * 单位时间产生的令牌个数
     *
     * @return int int
     */
    int rate();

    /**
     * 是否限制IP
     *
     * @return boolean boolean
     */
    boolean restrictIp() default false;
}
