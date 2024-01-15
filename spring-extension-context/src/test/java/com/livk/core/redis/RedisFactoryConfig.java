package com.livk.core.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * <p>
 * RedisFactoryConfig
 * </p>
 *
 * @author livk
 */
@Configuration
@PropertySource("classpath:env.properties")
class RedisFactoryConfig {

	@Bean
	public LettuceConnectionFactory redisConnectionFactory(@Value("${redis.host}") String host,
			@Value("${redis.port}") int port, @Value("${redis.password}") String password) {
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
		configuration.setPassword(password);
		return new LettuceConnectionFactory(configuration);
	}

}
