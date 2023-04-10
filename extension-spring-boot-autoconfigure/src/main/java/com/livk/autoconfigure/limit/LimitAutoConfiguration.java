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
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * The type Limit auto configuration.
 *
 * @author livk
 */
@SpringAutoService(EnableLimit.class)
@AutoConfiguration
public class LimitAutoConfiguration {

    /**
     * Limit interceptor limit interceptor.
     *
     * @param limitExecutor the limit executor
     * @return the limit interceptor
     */
    @Bean
    @ConditionalOnBean(LimitExecutor.class)
    public LimitInterceptor limitInterceptor(LimitExecutor limitExecutor) {
        return new LimitInterceptor(limitExecutor);
    }

    /**
     * The type Redisson limit configuration.
     */
    @AutoConfiguration(after = {RedissonAutoConfiguration.class},
            afterName = {"org.redisson.spring.starter.RedissonAutoConfiguration"})
    public static class RedissonLimitConfiguration {
        /**
         * Redisson limit executor limit executor.
         *
         * @param redissonClient the redisson client
         * @return the limit executor
         */
        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnBean(RedissonClient.class)
        public LimitExecutor redissonLimitExecutor(RedissonClient redissonClient) {
            return new RedissonLimitExecutor(redissonClient);
        }
    }
}
