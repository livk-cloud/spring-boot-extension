package com.livk.autoconfigure.lock.local;

import com.livk.autoconfigure.lock.aspect.LockAspect;
import com.livk.autoconfigure.lock.support.DistributedLock;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * RedissonLockAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/9/5
 */
@AutoConfiguration
public class LocalLockAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LockAspect lockAspect(ObjectProvider<DistributedLock> distributedLockProvider) {
        return new LockAspect(distributedLockProvider);
    }

    @Bean
    public DistributedLock localLock() {
        return new LocalLock();
    }
}
