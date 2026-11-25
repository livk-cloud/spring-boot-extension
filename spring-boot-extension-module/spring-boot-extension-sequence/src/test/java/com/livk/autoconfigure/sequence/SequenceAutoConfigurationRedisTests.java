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
import com.livk.testcontainers.DockerImageNames;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class SequenceAutoConfigurationRedisTests {

	private static final int THREADS = 12;

	private static final int PER_THREAD = 40;

	@Container
	static final RedisContainer redis = new RedisContainer(DockerImageNames.redis());

	@Test
	void configuresRedisBackendAndIssuesUniqueIdsConcurrently() {
		new ApplicationContextRunner().withConfiguration(AutoConfigurations.of(SequenceAutoConfiguration.class))
			.withUserConfiguration(RedisConfig.class)
			.withPropertyValues("spring.sequence.type=redis", "spring.sequence.step=10",
					"spring.sequence.biz-name=autoconfig-redis", "spring.data.redis.host=" + redis.getHost(),
					"spring.data.redis.port=" + redis.getFirstMappedPort())
			.run((context) -> {
				assertThat(context).hasSingleBean(SequenceInterceptor.class);
				assertThat(context).hasSingleBean(RangeSequenceProvider.class);
				assertThat(context).hasSingleBean(Sequence.class);

				SequenceProvider provider = context.getBean(SequenceProvider.class);
				Sequence sequence = provider.get("autoconfig-redis-concurrent");

				Set<Long> ids = ConcurrentHashMap.newKeySet();
				AtomicReference<Throwable> error = new AtomicReference<>();
				CountDownLatch ready = new CountDownLatch(THREADS);
				CountDownLatch start = new CountDownLatch(1);
				CountDownLatch done = new CountDownLatch(THREADS);
				try (ExecutorService executor = Executors.newFixedThreadPool(THREADS)) {
					for (int i = 0; i < THREADS; i++) {
						executor.submit(() -> {
							ready.countDown();
							try {
								start.await();
								for (int j = 0; j < PER_THREAD; j++) {
									assertThat(ids.add(sequence.nextValue())).isTrue();
								}
							}
							catch (Throwable ex) {
								error.compareAndSet(null, ex);
							}
							finally {
								done.countDown();
							}
						});
					}
					assertThat(ready.await(10, TimeUnit.SECONDS)).isTrue();
					start.countDown();
					assertThat(done.await(30, TimeUnit.SECONDS)).isTrue();
				}

				assertThat(error.get()).isNull();
				assertThat(ids).hasSize(THREADS * PER_THREAD);
				assertThat(context.getBean(Sequence.class).nextValue()).isPositive();
			});
	}

	@EnableSequence
	@TestConfiguration(proxyBeanMethods = false)
	static class RedisConfig {

		@Bean(destroyMethod = "destroy")
		RedisConnectionFactory redisConnectionFactory(@Value("${spring.data.redis.host}") String host,
				@Value("${spring.data.redis.port}") int port) {
			LettuceConnectionFactory factory = new LettuceConnectionFactory(host, port);
			factory.afterPropertiesSet();
			return factory;
		}

	}

}
