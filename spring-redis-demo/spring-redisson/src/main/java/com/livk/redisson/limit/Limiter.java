package com.livk.redisson.limit;

import org.redisson.api.RateIntervalUnit;

import java.lang.annotation.*;

/**
 * <p>
 * Limiter
 * </p>
 *
 * @author livk
 * @date 2022/6/13
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limiter {

    String key();

    int rate();

    int rateInterval();

    RateIntervalUnit rateIntervalUnit() default RateIntervalUnit.SECONDS;

    int requestedTokens() default 1;

}
