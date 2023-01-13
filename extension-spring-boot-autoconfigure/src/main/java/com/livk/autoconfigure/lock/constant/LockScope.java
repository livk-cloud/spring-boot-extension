package com.livk.autoconfigure.lock.constant;

/**
 * <p>
 * LockScope
 * </p>
 *
 * @author livk
 */
public enum LockScope {
    /**
     * 本地锁
     */
    STANDALONE_LOCK,
    /**
     * 分布式锁
     */
    DISTRIBUTED_LOCK
}
