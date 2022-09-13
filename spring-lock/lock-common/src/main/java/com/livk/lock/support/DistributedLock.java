package com.livk.lock.support;

import com.livk.lock.constant.LockScope;
import com.livk.lock.constant.LockType;

/**
 * <p>
 * DistributedLock
 * </p>
 *
 * @author livk
 * @date 2022/9/5
 */
public interface DistributedLock {

    boolean tryLock(LockType type, String key, long leaseTime, long waitTime, boolean async);

    void lock(LockType type, String key, boolean async);

    void unlock();

    LockScope scope();
}
