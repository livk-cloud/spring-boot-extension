package com.livk.autoconfigure.lock;

import com.livk.autoconfigure.lock.aspect.LockAspect;
import com.livk.autoconfigure.lock.support.DistributedLock;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * LockAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/9/13
 */
@AutoConfiguration
public class LockAutoConfiguration {

    @Bean
    public LockAspect lockAspect(ObjectProvider<DistributedLock> distributedLockProvider) {
        return new LockAspect(distributedLockProvider);
    }
}
