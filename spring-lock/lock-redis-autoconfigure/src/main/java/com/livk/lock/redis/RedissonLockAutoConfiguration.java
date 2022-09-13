package com.livk.lock.redis;

import com.livk.lock.LockAutoConfiguration;
import com.livk.lock.support.DistributedLock;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * RedissonLockAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/9/5
 */
@AutoConfiguration(before = LockAutoConfiguration.class,
        after = RedissonAutoConfiguration.class)
public class RedissonLockAutoConfiguration {

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public DistributedLock redissonLock(RedissonClient redissonClient) {
        return new RedissonLock(redissonClient);
    }
}
