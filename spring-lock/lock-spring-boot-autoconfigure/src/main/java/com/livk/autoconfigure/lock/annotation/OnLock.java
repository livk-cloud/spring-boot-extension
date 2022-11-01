package com.livk.autoconfigure.lock.annotation;

import com.livk.autoconfigure.lock.constant.LockScope;
import com.livk.autoconfigure.lock.constant.LockType;

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

    /**
     * scope为{@link LockScope#STANDALONE_LOCK}
     * async强制为false
     */
    boolean async() default false;

    LockScope scope();
}
