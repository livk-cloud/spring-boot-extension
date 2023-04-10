package com.livk.autoconfigure.lock.support;

import com.livk.autoconfigure.lock.constant.LockScope;
import com.livk.autoconfigure.lock.constant.LockType;

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
    protected boolean tryLock(Lock lock, long leaseTime, long waitTime) throws Exception {
        return lock.tryLock(waitTime, TimeUnit.SECONDS);
    }

    @Override
    protected void lock(Lock lock) {
        lock.lock();
    }

    @Override
    protected boolean unlock(String key, Lock lock) {
        lock.unlock();
        return !isLocked(lock);
    }

    @Override
    protected boolean isLocked(Lock lock) {
        if (lock instanceof ReentrantLock reentrantLock) {
            return reentrantLock.isLocked() && reentrantLock.isHeldByCurrentThread();
        } else if (lock instanceof ReentrantReadWriteLock.WriteLock writeLock) {
            return writeLock.getHoldCount() != 0 && writeLock.isHeldByCurrentThread();
        }
        return false;
    }

    @Override
    public LockScope scope() {
        return LockScope.STANDALONE_LOCK;
    }
}
