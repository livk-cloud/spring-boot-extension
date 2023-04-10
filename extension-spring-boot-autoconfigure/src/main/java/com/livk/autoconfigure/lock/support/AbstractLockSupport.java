package com.livk.autoconfigure.lock.support;

import com.livk.autoconfigure.lock.constant.LockType;
import com.livk.autoconfigure.lock.exception.LockException;
import com.livk.autoconfigure.lock.exception.UnSupportLockException;
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
    protected final ThreadLocal<Pair<String, T>> threadLocal = new InheritableThreadLocal<>();

    @Override
    public boolean tryLock(LockType type, String key, long leaseTime, long waitTime, boolean async) {
        T lock = getLock(type, key);
        try {
            boolean isLocked = supportAsync() && async ?
                    tryLockAsync(lock, leaseTime, waitTime) :
                    tryLock(lock, waitTime, leaseTime);
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
            if (supportAsync() && async) {
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
            String key = pair.key();
            T lock = pair.value();
            if (isLocked(lock) && unlock(key, lock)) {
                threadLocal.remove();
            }
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
     * @return the boolean
     */
    protected abstract boolean unlock(String key, T lock);

    /**
     * Try lock async boolean.
     *
     * @param lock      the lock
     * @param leaseTime the lease time
     * @param waitTime  the wait time
     * @return the boolean
     * @throws Exception the exception
     */
    protected boolean tryLockAsync(T lock, long leaseTime, long waitTime) throws Exception {
        throw new UnSupportLockException("Async lock of " + this.getClass().getSimpleName() + " isn't support");
    }

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
    protected void lockAsync(T lock) throws Exception {
        throw new UnSupportLockException("Async lock of " + this.getClass().getSimpleName() + " isn't support");
    }

    /**
     * Lock.
     *
     * @param lock the lock
     * @throws Exception the exception
     */
    protected abstract void lock(T lock) throws Exception;

    /**
     * Is locked boolean.
     *
     * @param lock the lock
     * @return the boolean
     */
    protected abstract boolean isLocked(T lock);

    /**
     * Support async boolean.
     *
     * @return the boolean
     */
    protected boolean supportAsync() {
        return false;
    }
}
