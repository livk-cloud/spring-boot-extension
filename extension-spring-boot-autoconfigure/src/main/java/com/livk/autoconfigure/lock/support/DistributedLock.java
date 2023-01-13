package com.livk.autoconfigure.lock.support;

import com.livk.autoconfigure.lock.constant.LockScope;
import com.livk.autoconfigure.lock.constant.LockType;
import org.springframework.core.Ordered;

/**
 * <p>
 * DistributedLock
 * </p>
 *
 * @author livk
 */
public interface DistributedLock extends Ordered {

    /**
     * Try lock boolean.
     *
     * @param type      the type
     * @param key       the key
     * @param leaseTime the lease time
     * @param waitTime  the wait time
     * @param async     the async
     * @return the boolean
     */
    boolean tryLock(LockType type, String key, long leaseTime, long waitTime, boolean async);

    /**
     * Lock.
     *
     * @param type  the type
     * @param key   the key
     * @param async the async
     */
    void lock(LockType type, String key, boolean async);

    /**
     * Unlock.
     */
    void unlock();

    /**
     * Scope lock scope.
     *
     * @return the lock scope
     */
    LockScope scope();

    @Override
    default int getOrder() {
        return 0;
    }
}
