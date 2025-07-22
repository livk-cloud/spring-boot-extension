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

package com.livk.autoconfigure.redisson;

import com.livk.auto.service.annotation.SpringAutoService;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Fallback;

/**
 * The type Redisson auto configuration.
 *
 * @author livk
 */
@SpringAutoService
@ConditionalOnClass(Redisson.class)
@AutoConfiguration(before = RedisAutoConfiguration.class)
@EnableConfigurationProperties({ RedissonProperties.class, RedisProperties.class })
public class RedissonAutoConfiguration {

	@Bean
	public CustomEditorConfigurer customEditorConfigurer() {
		CustomEditorConfigurer configurer = new CustomEditorConfigurer();
		configurer.setPropertyEditorRegistrars(new PropertyEditorRegistrar[] { new RedissonPropertyEditorRegistrar() });
		return configurer;
	}

	/**
	 * RedissonClient
	 * @param properties the config properties
	 * @param configCustomizers the config customizers
	 * @return the redisson client
	 */
	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
	@Conditional(RedissonCondition.class)
	public RedissonClient redissonClient(RedissonProperties properties,
			ObjectProvider<ConfigCustomizer> configCustomizers) {
		return RedissonClientFactory.create(properties, configCustomizers);
	}

	@Fallback
	@Bean(value = "redissonClient", destroyMethod = "shutdown")
	@ConditionalOnMissingBean
	public RedissonClient fallbackRedissonClient(RedisProperties properties,
			ObjectProvider<ConfigCustomizer> configCustomizers) {
		return RedissonClientFactory.create(properties, configCustomizers);
	}

	/**
	 * Redisson connection factory redisson connection factory.
	 * @param redisson the redisson
	 * @return the redisson connection factory
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(RedissonConnectionFactory.class)
	public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
		return new RedissonConnectionFactory(redisson);
	}

}
