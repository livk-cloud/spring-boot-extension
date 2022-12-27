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
 * @date 2022/12/27
 */
@RequiredArgsConstructor
public class CuratorLock extends AbstractLockSupport<InterProcessLock> {

    private final CuratorFramework curatorFramework;

    @Override
    protected InterProcessLock getLock(LockType type, String key) {
        return switch (type) {
            case LOCK, FAIR -> new InterProcessMutex(curatorFramework, key);
            case READ -> new InterProcessReadWriteLock(curatorFramework, key).readLock();
            case WRITE -> new InterProcessReadWriteLock(curatorFramework, key).writeLock();
        };
    }

    @Override
    protected void unlock(String key, InterProcessLock lock) {
        if (lock != null && lock.isAcquiredInThisProcess()) {
            try {
                lock.release();
            } catch (Exception e) {
                throw new LockException(e);
            }
        }
    }

    @Override
    protected boolean tryLockAsync(InterProcessLock lock, long leaseTime, long waitTime) throws Exception {
        throw new LockException("Async lock of Curator isn't support");
    }

    @Override
    protected boolean tryLock(InterProcessLock lock, long leaseTime, long waitTime) throws Exception {
        return lock.acquire(waitTime, TimeUnit.SECONDS);
    }

    @Override
    protected void lockAsync(InterProcessLock lock) throws Exception {
        throw new LockException("Async lock of Curator isn't support");
    }

    @Override
    protected void lock(InterProcessLock lock) throws Exception {
        lock.acquire();
    }

    @Override
    public LockScope scope() {
        return LockScope.DISTRIBUTED_LOCK;
    }
}
