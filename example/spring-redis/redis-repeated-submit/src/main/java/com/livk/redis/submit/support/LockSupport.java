package com.livk.redis.submit.support;

import com.livk.autoconfigure.lock.constant.LockType;
import com.livk.autoconfigure.lock.support.DistributedLock;
import com.livk.autoconfigure.lock.support.RedissonLock;
import com.livk.commons.spring.context.SpringContextHolder;
import lombok.experimental.UtilityClass;

/**
 * @author livk
 */
@UtilityClass
public class LockSupport {

    private static final DistributedLock LOCK;

    static {
        LOCK = SpringContextHolder.getBean(RedissonLock.class);
    }

    public boolean tryLock(LockType type, String key, long leaseTime, long waitTime, boolean async) {
        return LOCK.tryLock(type, key, leaseTime, waitTime, async);
    }

    public void unlock() {
        LOCK.unlock();
    }
}
