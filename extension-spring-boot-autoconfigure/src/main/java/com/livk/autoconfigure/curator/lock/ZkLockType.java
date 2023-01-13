package com.livk.autoconfigure.curator.lock;

/**
 * <p>
 * ZkLockType
 * </p>
 *
 * @author livk
 */
public enum ZkLockType {
    /**
     * 普通锁
     */
    LOCK,
    /**
     * 读锁
     */
    READ,
    /**
     * 写锁
     */
    WRITE
}
