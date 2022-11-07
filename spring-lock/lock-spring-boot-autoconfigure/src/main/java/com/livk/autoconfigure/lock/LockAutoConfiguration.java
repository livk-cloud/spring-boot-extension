package com.livk.autoconfigure.lock;

import com.livk.autoconfigure.lock.aspect.LockAspect;
import com.livk.autoconfigure.lock.local.LocalLock;
import com.livk.autoconfigure.lock.redis.RedissonLock;
import com.livk.autoconfigure.lock.support.DistributedLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * LockAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/10/31
 */
@AutoConfiguration
public class LockAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LockAspect lockAspect(ObjectProvider<DistributedLock> distributedLockProvider) {
        return new LockAspect(distributedLockProvider);
    }

    @Bean
    public DistributedLock localLock() {
        return new LocalLock();
    }

    @ConditionalOnClass(RedissonClient.class)
    @AutoConfiguration(afterName = {"com.livk.autoconfigure.redisson.RedissonAutoConfiguration",
            "org.redisson.spring.starter.RedissonAutoConfiguration"})
    public static class RedissonLockAutoConfiguration {
        @Bean
        public DistributedLock redissonLock(RedissonClient redissonClient) {
            return new RedissonLock(redissonClient);
        }
    }
}
