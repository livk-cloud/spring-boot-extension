package com.livk.common.redis;

import com.livk.common.redis.supprot.LivkReactiveRedisTemplate;
import com.livk.common.redis.supprot.LivkRedisTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * <p>
 * LivkRedisAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2021/11/26
 */
@AutoConfiguration(before = { RedisAutoConfiguration.class })
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
