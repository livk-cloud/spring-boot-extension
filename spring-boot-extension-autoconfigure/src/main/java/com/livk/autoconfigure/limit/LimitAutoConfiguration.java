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

package com.livk.autoconfigure.limit;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.redisson.RedissonAutoConfiguration;
import com.livk.context.limit.LimitExecutor;
import com.livk.context.limit.executor.RedissonLimitExecutor;
import com.livk.context.limit.interceptor.LimitInterceptor;
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
	@AutoConfiguration(after = { RedissonAutoConfiguration.class },
			afterName = { "org.redisson.spring.starter.RedissonAutoConfiguration" })
	public static class RedissonLimitConfiguration {

		/**
		 * Redisson limit executor limit executor.
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
