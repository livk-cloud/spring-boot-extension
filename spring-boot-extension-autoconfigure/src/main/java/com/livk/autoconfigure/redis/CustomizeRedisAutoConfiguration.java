/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.autoconfigure.redis;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.core.redis.JacksonSerializerUtils;
import com.livk.core.redis.ReactiveRedisOps;
import com.livk.core.redis.RedisOps;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import reactor.core.publisher.Flux;

/**
 * The type Customize redis auto configuration.
 *
 * @author livk
 */
@SpringAutoService
@ConditionalOnClass(RedisOperations.class)
@AutoConfiguration(before = { RedisAutoConfiguration.class })
public class CustomizeRedisAutoConfiguration {

	/**
	 * Livk redis template universal redis template.
	 * @param redisConnectionFactory the redis connection factory
	 * @return the universal redis template
	 */
	@Bean
	@ConditionalOnMissingBean
	public RedisOps redisOps(RedisConnectionFactory redisConnectionFactory) {
		return new RedisOps(redisConnectionFactory);
	}

	/**
	 * The type Customize reactive redis auto configuration.
	 */
	@AutoConfiguration(before = { RedisReactiveAutoConfiguration.class })
	@ConditionalOnClass({ ReactiveRedisConnectionFactory.class, ReactiveRedisTemplate.class, Flux.class })
	public static class CustomizeReactiveRedisAutoConfiguration {

		/**
		 * Universal reactive redis template universal reactive redis template.
		 * @param redisConnectionFactory the redis connection factory
		 * @return the universal reactive redis template
		 */
		@Bean
		@ConditionalOnMissingBean
		public ReactiveRedisOps reactiveRedisOps(ReactiveRedisConnectionFactory redisConnectionFactory) {
			RedisSerializationContext<String, Object> serializationContext = RedisSerializationContext
				.<String, Object>newSerializationContext()
				.key(RedisSerializer.string())
				.value(JacksonSerializerUtils.json())
				.hashKey(RedisSerializer.string())
				.hashValue(JacksonSerializerUtils.json())
				.build();
			return new ReactiveRedisOps(redisConnectionFactory, serializationContext);
		}

	}

}
