package com.livk.autoconfigure.lock.support;

import com.livk.autoconfigure.lock.constant.LockType;
import com.livk.autoconfigure.lock.exception.LockException;
import com.livk.commons.domain.Pair;

import java.util.concurrent.locks.Lock;

/**
 * <p>
 * AbstractLock
 * </p>
 *
 * @author livk
 *
 */
public abstract class AbstractLockSupport<T extends Lock> implements DistributedLock {
    protected final ThreadLocal<Pair<String, T>> threadLocal = new ThreadLocal<>();

    @Override
    public boolean tryLock(LockType type, String key, long leaseTime, long waitTime, boolean async) {
        T lock = getLock(type, key);
        try {
            boolean isLocked = async ? tryLockAsync(lock, leaseTime, waitTime) : tryLock(lock, waitTime, leaseTime);
            if (isLocked) {
                threadLocal.set(Pair.of(key, lock));
            }
            return isLocked;
        } catch (Exception e) {
            threadLocal.remove();
            throw new LockException(e);
        }
    }

    @Override
    public void lock(LockType type, String key, boolean async) {
        T lock = getLock(type, key);
        try {
            if (async) {
                lockAsync(lock);
            } else {
                lock(lock);
            }
            threadLocal.set(Pair.of(key, lock));
        } catch (Exception e) {
            threadLocal.remove();
            throw new LockException(e);
        }
    }

    @Override
    public void unlock() {
        Pair<String, T> pair = threadLocal.get();
        if (pair != null) {
            unlock(pair.key(), pair.value());
            threadLocal.remove();
        }
    }


    protected abstract T getLock(LockType type, String key);

    protected abstract void unlock(String key, T lock);

    protected abstract boolean tryLockAsync(T lock, long leaseTime, long waitTime) throws Exception;

    protected abstract boolean tryLock(T lock, long leaseTime, long waitTime) throws Exception;

    protected abstract void lockAsync(T lock) throws Exception;

    protected abstract void lock(T lock);
}
