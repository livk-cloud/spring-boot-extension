package com.livk.caffeine.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * CaffeineConfig
 * </p>
 *
 * @author livk
 * @date 2022/3/30
 */
@Configuration
public class CaffeineConfig {

	@Bean
	public Cache<String, Object> caffeineCache() {
		return Caffeine.newBuilder().initialCapacity(128).maximumSize(1024).expireAfterWrite(60, TimeUnit.SECONDS)
				.build();
	}

}
