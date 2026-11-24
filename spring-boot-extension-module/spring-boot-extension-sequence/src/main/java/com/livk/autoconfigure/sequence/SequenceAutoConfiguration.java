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

package com.livk.autoconfigure.sequence;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.context.sequence.Sequence;
import com.livk.context.sequence.SequenceProvider;
import com.livk.context.sequence.interceptor.SequenceInterceptor;
import com.livk.context.sequence.support.DbRangeManager;
import com.livk.context.sequence.support.RangeSequenceProvider;
import com.livk.context.sequence.support.RedisRangeManager;
import com.livk.context.sequence.support.SnowflakeSequenceProvider;
import com.livk.context.sequence.support.redis.SpringSequenceRedisHelper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import javax.sql.DataSource;

/**
 * Auto-configuration for distributed sequence support.
 *
 * @author livk
 */
@SpringAutoService(EnableSequence.class)
@AutoConfiguration
@EnableConfigurationProperties(SequenceProperties.class)
public class SequenceAutoConfiguration {

	/**
	 * Sequence interceptor.
	 * @param providers available sequence providers
	 * @return the interceptor
	 */
	@Bean
	@ConditionalOnMissingBean
	public SequenceInterceptor sequenceInterceptor(ObjectProvider<SequenceProvider> providers) {
		return new SequenceInterceptor(providers);
	}

	/**
	 * A single sequence for the configured business name.
	 * @param provider the sequence provider
	 * @param properties the sequence properties
	 * @return the sequence
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = SequenceProperties.PREFIX, name = "biz-name")
	public Sequence sequence(SequenceProvider provider, SequenceProperties properties) {
		return provider.get(properties.getBizName());
	}

	/**
	 * Database range sequence.
	 */
	@ConditionalOnClass(DataSource.class)
	@ConditionalOnBean(DataSource.class)
	@ConditionalOnProperty(prefix = SequenceProperties.PREFIX, name = "type", havingValue = "db")
	@AutoConfiguration
	public static class DbSequenceConfiguration {

		/**
		 * Database sequence provider.
		 * @param dataSource the data source
		 * @param properties the sequence properties
		 * @return the provider
		 */
		@Bean
		@ConditionalOnMissingBean(SequenceProvider.class)
		public RangeSequenceProvider dbSequenceProvider(DataSource dataSource, SequenceProperties properties) {
			return new RangeSequenceProvider(new DbRangeManager(dataSource), properties.getStep(),
					properties.getStepStart());
		}

	}

	/**
	 * Redis range sequence.
	 */
	@ConditionalOnClass(RedisConnectionFactory.class)
	@ConditionalOnBean(RedisConnectionFactory.class)
	@ConditionalOnProperty(prefix = SequenceProperties.PREFIX, name = "type", havingValue = "redis")
	@AutoConfiguration
	public static class RedisSequenceConfiguration {

		/**
		 * Redis sequence provider.
		 * @param connectionFactory the redis connection factory
		 * @param properties the sequence properties
		 * @return the provider
		 */
		@Bean
		@ConditionalOnMissingBean(SequenceProvider.class)
		public RangeSequenceProvider redisSequenceProvider(RedisConnectionFactory connectionFactory,
				SequenceProperties properties) {
			SpringSequenceRedisHelper helper = new SpringSequenceRedisHelper(connectionFactory);
			return new RangeSequenceProvider(new RedisRangeManager(helper), properties.getStep(),
					properties.getStepStart());
		}

	}

	/**
	 * Snowflake sequence used when no other type is selected.
	 */
	@ConditionalOnProperty(prefix = SequenceProperties.PREFIX, name = "type", havingValue = "snowflake",
			matchIfMissing = true)
	@AutoConfiguration
	public static class SnowflakeSequenceConfiguration {

		/**
		 * Snowflake sequence provider.
		 * @param properties the sequence properties
		 * @return the provider
		 */
		@Bean
		@ConditionalOnMissingBean(SequenceProvider.class)
		public SnowflakeSequenceProvider snowflakeSequenceProvider(SequenceProperties properties) {
			return new SnowflakeSequenceProvider(properties.getDataCenterId(), properties.getMachineId());
		}

	}

}
