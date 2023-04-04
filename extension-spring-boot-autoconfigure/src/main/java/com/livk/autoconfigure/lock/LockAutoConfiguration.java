package com.livk.autoconfigure.lock;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.curator.CuratorAutoConfiguration;
import com.livk.autoconfigure.lock.intercept.LockInterceptor;
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

    /**
     * Lock aspect lock aspect.
     *
     * @param distributedLockProvider the distributed lock provider
     * @return the lock aspect
     */
    @Bean
    @ConditionalOnMissingBean
    public LockInterceptor lockInterceptor(ObjectProvider<DistributedLock> distributedLockProvider) {
        return new LockInterceptor(distributedLockProvider);
    }

    /**
     * Local lock distributed lock.
     *
     * @return the distributed lock
     */
    @Bean
    public DistributedLock localLock() {
        return new LocalLock();
    }

    /**
     * The type Redisson lock auto configuration.
     */
    @ConditionalOnClass(RedissonClient.class)
    @AutoConfiguration(after = RedissonAutoConfiguration.class,
            afterName = {"org.redisson.spring.starter.RedissonAutoConfiguration"})
    public static class RedissonLockAutoConfiguration {
        /**
         * Redisson lock distributed lock.
         *
         * @param redissonClient the redisson client
         * @return the distributed lock
         */
        @Bean
        public DistributedLock redissonLock(RedissonClient redissonClient) {
            return new RedissonLock(redissonClient);
        }
    }

    /**
     * The type Curator lock auto configuration.
     */
    @ConditionalOnClass(CuratorFramework.class)
    @AutoConfiguration(after = CuratorAutoConfiguration.class)
    public static class CuratorLockAutoConfiguration {
        /**
         * Redisson lock distributed lock.
         *
         * @param curatorFramework the curator framework
         * @return the distributed lock
         */
        @Bean
        public DistributedLock redissonLock(CuratorFramework curatorFramework) {
            return new CuratorLock(curatorFramework);
        }
    }
}
