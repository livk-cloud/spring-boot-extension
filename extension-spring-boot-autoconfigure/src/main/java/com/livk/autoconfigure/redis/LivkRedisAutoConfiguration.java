package com.livk.autoconfigure.redis;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.redis.supprot.LivkReactiveRedisTemplate;
import com.livk.autoconfigure.redis.supprot.LivkRedisTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;

/**
 * <p>
 * LivkRedisAutoConfiguration
 * </p>
 *
 * @author livk
 *
 */
@SpringAutoService
@ConditionalOnClass(RedisOperations.class)
@AutoConfiguration(before = {RedisAutoConfiguration.class})
public class LivkRedisAutoConfiguration {

    @Bean
    public LivkRedisTemplate livkRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new LivkRedisTemplate(redisConnectionFactory);
    }

    @Bean
    public LivkReactiveRedisTemplate livkReactiveRedisTemplate(ReactiveRedisConnectionFactory redisConnectionFactory) {
        return new LivkReactiveRedisTemplate(redisConnectionFactory);
    }

}
