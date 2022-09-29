package com.livk.lock.local;

import com.livk.lock.LockAutoConfiguration;
import com.livk.lock.support.DistributedLock;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * RedissonLockAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/9/5
 */
@AutoConfiguration(before = LockAutoConfiguration.class)
public class LocalLockAutoConfiguration {

    @Bean
    public DistributedLock localLock() {
        return new LocalLock();
    }
}
