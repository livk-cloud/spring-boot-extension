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

package com.livk.autoconfigure.idempotent;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.redisson.RedissonAutoConfiguration;
import com.livk.context.idempotent.IdempotentExecutor;
import com.livk.context.idempotent.executor.RedissonIdempotentExecutor;
import com.livk.context.idempotent.interceptor.IdempotentInterceptor;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration for idempotent method support.
 *
 * @author livk
 */
@SpringAutoService(EnableIdempotent.class)
@AutoConfiguration
public class IdempotentAutoConfiguration {

	/**
	 * Idempotent interceptor.
	 * @param executors available token stores
	 * @return the interceptor
	 */
	@Bean
	@ConditionalOnMissingBean
	public IdempotentInterceptor idempotentInterceptor(ObjectProvider<IdempotentExecutor> executors) {
		return new IdempotentInterceptor(executors);
	}

	/**
	 * Redisson-backed idempotent token store.
	 */
	@ConditionalOnClass(RedissonClient.class)
	@ConditionalOnBean(RedissonClient.class)
	@AutoConfiguration(after = RedissonAutoConfiguration.class,
			afterName = { "org.redisson.spring.starter.RedissonAutoConfiguration" })
	public static class RedissonIdempotentConfiguration {

		/**
		 * Redisson idempotent executor.
		 * @param redissonClient the redisson client
		 * @return the executor
		 */
		@Bean
		@ConditionalOnMissingBean
		public RedissonIdempotentExecutor redissonIdempotentExecutor(RedissonClient redissonClient) {
			return new RedissonIdempotentExecutor(redissonClient);
		}

	}

}
