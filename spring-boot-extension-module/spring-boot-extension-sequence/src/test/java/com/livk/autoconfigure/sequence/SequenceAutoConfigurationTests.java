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

import com.livk.context.sequence.Sequence;
import com.livk.context.sequence.SequenceProvider;
import com.livk.context.sequence.interceptor.SequenceInterceptor;
import com.livk.context.sequence.support.RangeSequenceProvider;
import com.livk.context.sequence.support.SnowflakeSequenceProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author livk
 */
class SequenceAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(SequenceAutoConfiguration.class))
		.withUserConfiguration(Enabled.class);

	@Test
	void defaultsToSnowflake() {
		this.contextRunner.run((context) -> {
			assertThat(context).hasSingleBean(SequenceInterceptor.class);
			assertThat(context).hasSingleBean(SnowflakeSequenceProvider.class);
			assertThat(context).doesNotHaveBean(RangeSequenceProvider.class);
			assertThat(context).doesNotHaveBean(Sequence.class);
		});
	}

	@Test
	void exposesSequenceWhenBizNameIsSet() {
		this.contextRunner.withPropertyValues("spring.sequence.biz-name=order").run((context) -> {
			assertThat(context).hasSingleBean(Sequence.class);
			assertThat(context.getBean(Sequence.class).nextValue()).isPositive();
		});
	}

	@Test
	void usesDatabaseWhenRequested() {
		this.contextRunner.withPropertyValues("spring.sequence.type=db")
			.withUserConfiguration(DataSourceConfiguration.class)
			.run((context) -> {
				assertThat(context).hasSingleBean(RangeSequenceProvider.class);
				assertThat(context).doesNotHaveBean(SnowflakeSequenceProvider.class);
			});
	}

	@Test
	void usesDatabaseWhenTypeIsUpperCase() {
		this.contextRunner.withPropertyValues("spring.sequence.type=DB")
			.withUserConfiguration(DataSourceConfiguration.class)
			.run((context) -> {
				assertThat(context).hasSingleBean(RangeSequenceProvider.class);
				assertThat(context).doesNotHaveBean(SnowflakeSequenceProvider.class);
			});
	}

	@Test
	void skipsDatabaseProviderWithoutDataSource() {
		this.contextRunner.withPropertyValues("spring.sequence.type=db").run((context) -> {
			assertThat(context).doesNotHaveBean(SequenceProvider.class);
			assertThat(context).hasSingleBean(SequenceInterceptor.class);
		});
	}

	@Test
	void usesRedisWhenRequested() {
		this.contextRunner.withPropertyValues("spring.sequence.type=redis")
			.withUserConfiguration(RedisConfiguration.class)
			.run((context) -> {
				assertThat(context).hasSingleBean(RangeSequenceProvider.class);
				assertThat(context).doesNotHaveBean(SnowflakeSequenceProvider.class);
			});
	}

	@Test
	void selectsFirstProviderWhenBizNameIsSet() {
		this.contextRunner.withPropertyValues("spring.sequence.biz-name=order")
			.withUserConfiguration(ExtraProviderConfiguration.class)
			.run((context) -> {
				assertThat(context).hasSingleBean(Sequence.class);
				assertThat(context.getBean(Sequence.class).nextValue()).isEqualTo(99L);
			});
	}

	@Test
	void usesExplicitSnowflakeMachineId() {
		this.contextRunner.withPropertyValues("spring.sequence.machine-id=7").run((context) -> {
			assertThat(context).hasSingleBean(SnowflakeSequenceProvider.class);
			assertThat(context.getBean(SnowflakeSequenceProvider.class).get("order").nextValue()).isPositive();
		});
	}

	@EnableSequence
	@Configuration(proxyBeanMethods = false)
	static class Enabled {

	}

	@Configuration(proxyBeanMethods = false)
	static class ExtraProviderConfiguration {

		@Bean
		SequenceProvider highPriorityProvider() {
			return provider(99L, Ordered.HIGHEST_PRECEDENCE);
		}

		@Bean
		SequenceProvider lowPriorityProvider() {
			return provider(1L, Ordered.LOWEST_PRECEDENCE);
		}

		private static SequenceProvider provider(long id, int order) {
			Sequence sequence = mock(Sequence.class);
			given(sequence.nextValue()).willReturn(id);
			return new SequenceProvider() {
				@Override
				public Sequence get(String bizName) {
					return sequence;
				}

				@Override
				public int getOrder() {
					return order;
				}
			};
		}

	}

	@Configuration(proxyBeanMethods = false)
	static class DataSourceConfiguration {

		@Bean
		DataSource dataSource() {
			return new DriverManagerDataSource("jdbc:h2:mem:sequence;DB_CLOSE_DELAY=-1");
		}

	}

	@Configuration(proxyBeanMethods = false)
	static class RedisConfiguration {

		@Bean
		RedisConnectionFactory redisConnectionFactory() {
			RedisConnectionFactory factory = mock(RedisConnectionFactory.class);
			when(factory.getConnection()).thenReturn(mock(RedisConnection.class));
			return factory;
		}

	}

}
