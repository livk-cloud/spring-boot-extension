/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.redis.config;

import com.fasterxml.jackson.databind.JavaType;
import com.livk.common.redis.domain.LivkMessage;
import com.livk.commons.jackson.TypeFactoryUtils;
import com.livk.context.redis.JacksonSerializerUtils;
import com.livk.redis.listener.KeyExpiredListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;

/**
 * @author livk
 */
@Slf4j
@Configuration
@ConditionalOnClass(RedisOperations.class)
public class RedisConfig {

	@Bean
	public ReactiveRedisMessageListenerContainer reactiveRedisMessageListenerContainer(
			ReactiveRedisConnectionFactory connectionFactory) {
		ReactiveRedisMessageListenerContainer container = new ReactiveRedisMessageListenerContainer(connectionFactory);
		JavaType javaType = TypeFactoryUtils.javaType(LivkMessage.class, Object.class);
		RedisSerializer<LivkMessage<Object>> serializer = JacksonSerializerUtils.json(javaType);
		container
			.receive(List.of(PatternTopic.of(LivkMessage.CHANNEL)),
					RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()),
					RedisSerializationContext.SerializationPair.fromSerializer(serializer))
			.map(ReactiveSubscription.Message::getMessage)
			.subscribe(obj -> log.info("message:{}", obj));
		return container;
	}

	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory factory) {
		RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
		listenerContainer.setConnectionFactory(factory);
		return listenerContainer;
	}

	@Bean
	public KeyExpiredListener keyExpiredListener(RedisMessageListenerContainer listenerContainer) {
		return new KeyExpiredListener(listenerContainer);
	}

}
