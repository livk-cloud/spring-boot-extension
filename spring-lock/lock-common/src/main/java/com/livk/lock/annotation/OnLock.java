package com.livk.lock.annotation;

import com.livk.lock.constant.LockScope;
import com.livk.lock.constant.LockType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Lock
 * </p>
 *
 * @author livk
 * @date 2022/9/5
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnLock {
    /**
     * 锁名称
     */
    String key();

    LockType type() default LockType.LOCK;

    long leaseTime() default 10;

    long waitTime() default 3;

    boolean async() default false;

    LockScope scope();
}
