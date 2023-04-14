package com.livk.autoconfigure.limit;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.limit.annotation.EnableLimit;
import com.livk.autoconfigure.limit.interceptor.LimitInterceptor;
import com.livk.autoconfigure.limit.support.LimitExecutor;
import com.livk.autoconfigure.limit.support.RedissonLimitExecutor;
import com.livk.autoconfigure.redisson.RedissonAutoConfiguration;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
     * @param provider the provider
     * @return the limit interceptor
     */
    @Bean
    public LimitInterceptor limitInterceptor(ObjectProvider<LimitExecutor> provider) {
        return new LimitInterceptor(provider);
    }

    /**
     * The type Redisson limit configuration.
     */
    @ConditionalOnClass(RedissonClient.class)
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
        public LimitExecutor redissonLimitExecutor(RedissonClient redissonClient) {
            return new RedissonLimitExecutor(redissonClient);
        }
    }
}
