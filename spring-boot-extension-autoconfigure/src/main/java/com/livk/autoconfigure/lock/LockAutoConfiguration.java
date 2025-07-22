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

package com.livk.autoconfigure.lock;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.curator.CuratorAutoConfiguration;
import com.livk.autoconfigure.redisson.RedissonAutoConfiguration;
import com.livk.context.lock.DistributedLock;
import com.livk.context.lock.intercept.DistributedLockInterceptor;
import com.livk.context.lock.support.CuratorLock;
import com.livk.context.lock.support.RedissonLock;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author livk
 */
@AutoConfiguration
@SpringAutoService
@ConditionalOnClass(name = "com.livk.lock.marker.LockMarker")
public class LockAutoConfiguration {

	/**
	 * DistributedLockInterceptor
	 * @param distributedLockProvider the distributed lock provider
	 * @return the lock aspect
	 */
	@Bean
	@ConditionalOnMissingBean
	public DistributedLockInterceptor distributedLockInterceptor(
			ObjectProvider<DistributedLock> distributedLockProvider) {
		return new DistributedLockInterceptor(distributedLockProvider);
	}

	/**
	 * The type Redisson lock auto configuration.
	 */
	@ConditionalOnClass(RedissonClient.class)
	@AutoConfiguration(after = RedissonAutoConfiguration.class,
			afterName = { "org.redisson.spring.starter.RedissonAutoConfiguration" })
	public static class RedissonLockAutoConfiguration {

		/**
		 * Redisson lock distributed lock.
		 * @param redissonClient the redisson client
		 * @return the distributed lock
		 */
		@Bean
		public DistributedLock redissonLock(RedissonClient redissonClient) {
			return new RedissonLock(redissonClient);
		}

	}

	/**
	 * The type Curator lock auto configuration.
	 */
	@ConditionalOnClass(CuratorFramework.class)
	@AutoConfiguration(after = CuratorAutoConfiguration.class)
	public static class CuratorLockAutoConfiguration {

		/**
		 * Redisson lock distributed lock.
		 * @param framework the curator framework
		 * @return the distributed lock
		 */
		@Bean
		public DistributedLock curatorLock(CuratorFramework framework) {
			return new CuratorLock(framework);
		}

	}

}
