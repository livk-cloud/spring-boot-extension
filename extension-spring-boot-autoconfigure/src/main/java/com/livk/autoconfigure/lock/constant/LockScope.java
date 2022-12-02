package com.livk.autoconfigure.lock.constant;

/**
 * <p>
 * LockScope
 * </p>
 *
 * @author livk
 * @date 2022/9/12
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
