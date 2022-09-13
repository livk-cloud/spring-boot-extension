package com.livk.lock.constant;

/**
 * <p>
 * LockType
 * </p>
 *
 * @author livk
 * @date 2022/9/5
 */
public enum LockType {
    /**
     * 普通锁
     */
    LOCK,
    /**
     * 可重入锁
     */
    REENTRANT,
    /**
     * 公平锁
     */
    FAIR,
    /**
     * 读锁
     */
    READ,
    /**
     * 写锁
     */
    WRITE
}
