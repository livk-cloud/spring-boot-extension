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
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnLock {
    /**
     * 锁名称
     *
     * @return the string
     */
    String key();

    /**
     * Type lock type.
     *
     * @return the lock type
     */
    LockType type() default LockType.LOCK;

    /**
     * Lease time long.
     *
     * @return the long
     */
    long leaseTime() default 10;

    /**
     * Wait time long.
     *
     * @return the long
     */
    long waitTime() default 3;

    /**
     * scope为{@link LockScope#STANDALONE_LOCK}
     * async强制为false
     *
     * @return the boolean
     */
    boolean async() default false;

    /**
     * Scope lock scope.
     *
     * @return the lock scope
     */
    LockScope scope();
}
