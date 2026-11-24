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

import com.livk.context.idempotent.IdempotentExecutor;
import com.livk.context.idempotent.executor.RedissonIdempotentExecutor;
import com.livk.context.idempotent.interceptor.IdempotentInterceptor;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author livk
 */
class IdempotentAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(IdempotentAutoConfiguration.class));

	@Test
	void registersInterceptorAndRedissonExecutor() {
		this.contextRunner.withUserConfiguration(Enabled.class)
			.withBean(RedissonClient.class, () -> mock(RedissonClient.class))
			.run((context) -> {
				assertThat(context).hasSingleBean(IdempotentInterceptor.class);
				assertThat(context).hasSingleBean(RedissonIdempotentExecutor.class);
			});
	}

	@Test
	void registersInterceptorWithoutExecutorWhenClientIsMissing() {
		this.contextRunner.withUserConfiguration(Enabled.class).run((context) -> {
			assertThat(context).hasSingleBean(IdempotentInterceptor.class);
			assertThat(context).doesNotHaveBean(RedissonIdempotentExecutor.class);
		});
	}

	@Test
	void skipsRedissonExecutorWhenCustomExecutorExists() {
		this.contextRunner.withUserConfiguration(Enabled.class, CustomExecutorConfig.class)
			.withBean(RedissonClient.class, () -> mock(RedissonClient.class))
			.run((context) -> {
				assertThat(context).hasSingleBean(IdempotentExecutor.class);
				assertThat(context).doesNotHaveBean(RedissonIdempotentExecutor.class);
				assertThat(context.getBean(IdempotentExecutor.class)).isInstanceOf(CustomExecutor.class);
			});
	}

	@EnableIdempotent
	@Configuration(proxyBeanMethods = false)
	static class Enabled {

	}

	@Configuration(proxyBeanMethods = false)
	static class CustomExecutorConfig {

		@Bean
		IdempotentExecutor customIdempotentExecutor() {
			return new CustomExecutor();
		}

	}

	static final class CustomExecutor implements IdempotentExecutor {

		@Override
		public String tryAcquire(String key, Duration ttl) {
			return "custom";
		}

		@Override
		public void release(String key, String token) {
		}

	}

}
