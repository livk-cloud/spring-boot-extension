package com.livk.autoconfigure.lock.support;

import com.livk.autoconfigure.lock.constant.LockType;
import com.livk.autoconfigure.lock.exception.LockException;
import com.livk.commons.bean.domain.Pair;

/**
 * <p>
 * AbstractLock
 * </p>
 *
 * @param <T> the type parameter
 * @author livk
 */
public abstract class AbstractLockSupport<T> implements DistributedLock {
    /**
     * The Thread local.
     */
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


    /**
     * Gets lock.
     *
     * @param type the type
     * @param key  the key
     * @return the lock
     */
    protected abstract T getLock(LockType type, String key);

    /**
     * Unlock.
     *
     * @param key  the key
     * @param lock the lock
     */
    protected abstract void unlock(String key, T lock);

    /**
     * Try lock async boolean.
     *
     * @param lock      the lock
     * @param leaseTime the lease time
     * @param waitTime  the wait time
     * @return the boolean
     * @throws Exception the exception
     */
    protected abstract boolean tryLockAsync(T lock, long leaseTime, long waitTime) throws Exception;

    /**
     * Try lock boolean.
     *
     * @param lock      the lock
     * @param leaseTime the lease time
     * @param waitTime  the wait time
     * @return the boolean
     * @throws Exception the exception
     */
    protected abstract boolean tryLock(T lock, long leaseTime, long waitTime) throws Exception;

    /**
     * Lock async.
     *
     * @param lock the lock
     * @throws Exception the exception
     */
    protected abstract void lockAsync(T lock) throws Exception;

    /**
     * Lock.
     *
     * @param lock the lock
     * @throws Exception the exception
     */
    protected abstract void lock(T lock) throws Exception;
}
