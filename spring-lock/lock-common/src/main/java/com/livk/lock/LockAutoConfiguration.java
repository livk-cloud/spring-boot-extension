package com.livk.lock;

import com.livk.lock.aspect.LockAspect;
import com.livk.lock.support.DistributedLock;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

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
    public LockAspect lockAspect(List<DistributedLock> distributedLocks) {
        return new LockAspect(distributedLocks);
    }
}
