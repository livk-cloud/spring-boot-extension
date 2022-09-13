package com.livk.lock.support;

import com.livk.common.Pair;
import com.livk.lock.constant.LockType;
import com.livk.lock.exception.LockException;

import java.util.concurrent.locks.Lock;

/**
 * <p>
 * AbstractLock
 * </p>
 *
 * @author livk
 * @date 2022/9/5
 */
public abstract class AbstractLock<T extends Lock> implements DistributedLock {
    protected final ThreadLocal<Pair<String, T>> threadLocal = new ThreadLocal<>();

    protected T localLock(LockType type, String key) {
        T lock = getLock(type, key);
        threadLocal.set(Pair.of(key, lock));
        return lock;
    }

    @Override
    public boolean tryLock(LockType type, String key, long leaseTime, long waitTime, boolean async) {
        T lock = localLock(type, key);
        try {
            return async ? tryLockAsync(lock, leaseTime, waitTime) : tryLock(lock, waitTime, leaseTime);
        } catch (Exception e) {
            throw new LockException(e);
        }
    }

    @Override
    public void lock(LockType type, String key, boolean async) {
        T lock = localLock(type, key);
        try {
            if (async) {
                lockAsync(lock);
            } else {
                lock(lock);
            }
        } catch (Exception e) {
            throw new LockException(e);
        }
    }

    @Override
    public void unlock() {
        Pair<String, T> pair = threadLocal.get();
        if (pair != null) {
            unlock(pair.value());
            threadLocal.remove();
        }
    }


    protected abstract T getLock(LockType type, String key);

    protected abstract void unlock(T lock);

    protected abstract boolean tryLockAsync(T lock, long leaseTime, long waitTime) throws Exception;

    protected abstract boolean tryLock(T lock, long leaseTime, long waitTime) throws Exception;

    protected abstract void lockAsync(T lock) throws Exception;

    protected abstract void lock(T lock) throws Exception;
}
