package com.livk.autoconfigure.lock.support;

import com.livk.autoconfigure.lock.constant.LockScope;
import com.livk.autoconfigure.lock.constant.LockType;
import com.livk.autoconfigure.lock.exception.LockException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>
 * LocalLock
 * </p>
 *
 * @author livk
 *
 */
public class LocalLock extends AbstractLockSupport<Lock> {

    private static final Map<String, Lock> CACHE_LOCK = new ConcurrentHashMap<>();

    @Override
    protected Lock getLock(LockType type, String key) {
        return CACHE_LOCK.computeIfAbsent(key, s -> switch (type) {
            case LOCK -> new ReentrantLock();
            case FAIR -> new ReentrantLock(true);
            case READ -> new ReentrantReadWriteLock().readLock();
            case WRITE -> new ReentrantReadWriteLock().writeLock();
        });
    }

    @Override
    protected boolean tryLockAsync(Lock lock, long leaseTime, long waitTime) {
        throw new LockException("Async lock of Local isn't support");
    }

    @Override
    protected boolean tryLock(Lock lock, long leaseTime, long waitTime) throws Exception {
        return lock.tryLock(waitTime, TimeUnit.SECONDS);
    }

    @Override
    protected void lockAsync(Lock lock) {
        throw new LockException("Async lock of Local isn't support");
    }

    @Override
    protected void lock(Lock lock) {
        lock.lock();
    }

    @Override
    protected void unlock(String key, Lock lock) {
        if (lock != null) {
            if (lock instanceof ReentrantLock reentrantLock) {
                if (reentrantLock.isLocked() && reentrantLock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            } else if (lock instanceof ReentrantReadWriteLock.WriteLock writeLock) {
                if (writeLock.getHoldCount() == 0 && writeLock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            } else {
                lock.unlock();
            }
        }
        CACHE_LOCK.remove(key);
    }

    @Override
    public LockScope scope() {
        return LockScope.STANDALONE_LOCK;
    }
}
