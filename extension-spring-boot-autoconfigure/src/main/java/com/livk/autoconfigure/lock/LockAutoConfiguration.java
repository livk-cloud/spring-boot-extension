package com.livk.autoconfigure.lock;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.curator.CuratorAutoConfiguration;
import com.livk.autoconfigure.lock.aspect.LockAspect;
import com.livk.autoconfigure.lock.support.CuratorLock;
import com.livk.autoconfigure.lock.support.DistributedLock;
import com.livk.autoconfigure.lock.support.LocalLock;
import com.livk.autoconfigure.lock.support.RedissonLock;
import com.livk.autoconfigure.redisson.RedissonAutoConfiguration;
import org.apache.curator.framework.CuratorFramework;
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
 */
@AutoConfiguration
@SpringAutoService
@ConditionalOnClass(name = "com.livk.lock.marker.LockMarker")
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
    @AutoConfiguration(after = RedissonAutoConfiguration.class,
            afterName = {"org.redisson.spring.starter.RedissonAutoConfiguration"})
    public static class RedissonLockAutoConfiguration {
        @Bean
        public DistributedLock redissonLock(RedissonClient redissonClient) {
            return new RedissonLock(redissonClient);
        }
    }

    @ConditionalOnClass(CuratorFramework.class)
    @AutoConfiguration(after = CuratorAutoConfiguration.class)
    public static class CuratorLockAutoConfiguration {
        @Bean
        public DistributedLock redissonLock(CuratorFramework curatorFramework) {
            return new CuratorLock(curatorFramework);
        }
    }
}
