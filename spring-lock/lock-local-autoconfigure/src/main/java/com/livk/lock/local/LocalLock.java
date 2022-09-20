package com.livk.lock.local;

import com.livk.lock.constant.LockScope;
import com.livk.lock.constant.LockType;
import com.livk.lock.exception.LockException;
import com.livk.lock.support.AbstractLock;

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
 * @date 2022/9/6
 */
public class LocalLock extends AbstractLock<Lock> {
    @Override
    protected Lock getLock(LockType type, String key) {
        return switch (type) {
            case LOCK -> new ReentrantLock();
            case FAIR -> new ReentrantLock(true);
            case READ -> new ReentrantReadWriteLock().readLock();
            case WRITE -> new ReentrantReadWriteLock().writeLock();
        };
    }

    @Override
    protected boolean tryLockAsync(Lock lock, long leaseTime, long waitTime) throws Exception {
        throw new LockException("Async lock of Local isn't support");
    }

    @Override
    protected boolean tryLock(Lock lock, long leaseTime, long waitTime) throws Exception {
        return lock.tryLock(waitTime, TimeUnit.SECONDS);
    }

    @Override
    protected void lockAsync(Lock lock) throws Exception {
        throw new LockException("Async lock of Local isn't support");
    }

    @Override
    protected void lock(Lock lock) throws Exception {
        lock.lock();
    }

    @Override
    protected void unlock(Lock lock) {
        if (lock != null) {
            lock.unlock();
        }
    }

    @Override
    public LockScope scope() {
        return LockScope.STANDALONE_LOCK;
    }
}
