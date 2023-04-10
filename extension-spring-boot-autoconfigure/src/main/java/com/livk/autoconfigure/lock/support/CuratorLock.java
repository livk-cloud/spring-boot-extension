package com.livk.autoconfigure.lock.support;

import com.livk.autoconfigure.lock.constant.LockScope;
import com.livk.autoconfigure.lock.constant.LockType;
import com.livk.autoconfigure.lock.exception.LockException;
import lombok.RequiredArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * CuratorLock
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public class CuratorLock extends AbstractLockSupport<InterProcessLock> {

    private final CuratorFramework curatorFramework;

    @Override
    protected InterProcessLock getLock(LockType type, String key) {
        if (!key.startsWith("/")) {
            key = "/".concat(key);
        }
        return switch (type) {
            case LOCK, FAIR -> new InterProcessMutex(curatorFramework, key);
            case READ -> new InterProcessReadWriteLock(curatorFramework, key).readLock();
            case WRITE -> new InterProcessReadWriteLock(curatorFramework, key).writeLock();
        };
    }

    @Override
    protected boolean unlock(String key, InterProcessLock lock) {
        try {
            lock.release();
            return !isLocked(lock);
        } catch (Exception e) {
            throw new LockException(e);
        }
    }

    @Override
    protected boolean tryLock(InterProcessLock lock, long leaseTime, long waitTime) throws Exception {
        return lock.acquire(waitTime, TimeUnit.SECONDS);
    }

    @Override
    protected void lock(InterProcessLock lock) throws Exception {
        lock.acquire();
    }

    @Override
    protected boolean isLocked(InterProcessLock lock) {
        return lock.isAcquiredInThisProcess();
    }

    @Override
    public LockScope scope() {
        return LockScope.DISTRIBUTED_LOCK;
    }
}
