package com.livk.autoconfigure.limit;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.limit.annotation.EnableLimit;
import com.livk.autoconfigure.limit.interceptor.LimitInterceptor;
import com.livk.autoconfigure.limit.support.LimitExecutor;
import com.livk.autoconfigure.limit.support.RedissonLimitExecutor;
import com.livk.autoconfigure.redisson.RedissonAutoConfiguration;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * @author livk
 */
@SpringAutoService(EnableLimit.class)
@AutoConfiguration
public class LimitAutoConfiguration {

    @Bean
    @ConditionalOnBean(LimitExecutor.class)
    public LimitInterceptor limitInterceptor(LimitExecutor limitExecutor) {
        return new LimitInterceptor(limitExecutor);
    }

    @AutoConfiguration(after = {RedissonAutoConfiguration.class},
            afterName = {"org.redisson.spring.starter.RedissonAutoConfiguration"})
    public static class RedissonLimitConfiguration {
        @Bean
        @ConditionalOnBean(RedissonClient.class)
        public LimitExecutor redisLimitExecutor(RedissonClient redissonClient) {
            return new RedissonLimitExecutor(redissonClient);
        }
    }
}
