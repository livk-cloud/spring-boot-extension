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

package com.livk.context.sequence.support;

import com.livk.context.sequence.Sequence;
import com.livk.context.sequence.support.redis.SpringSequenceRedisHelper;
import com.livk.testcontainers.DockerImageNames;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
class RangeSequenceProviderRedisTests {

	private static final int THREADS = 16;

	private static final int PER_THREAD = 50;

	@Container
	static final RedisContainer redis = new RedisContainer(DockerImageNames.redis());

	static LettuceConnectionFactory factory;

	@BeforeAll
	static void setUp() {
		redis.start();
		factory = new LettuceConnectionFactory(redis.getHost(), redis.getFirstMappedPort());
		factory.afterPropertiesSet();
	}

	@AfterAll
	static void close() {
		if (factory != null) {
			factory.destroy();
		}
		redis.stop();
	}

	@Test
	void issuesUniqueIdsUnderConcurrentLoad() throws Exception {
		SpringSequenceRedisHelper helper = new SpringSequenceRedisHelper(factory);
		RangeSequenceProvider provider = new RangeSequenceProvider(new RedisRangeManager(helper), 8, 0);
		Sequence sequence = provider.get("concurrent-redis");

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
		assertThat(ids).allMatch((id) -> id > 0);
	}

}
