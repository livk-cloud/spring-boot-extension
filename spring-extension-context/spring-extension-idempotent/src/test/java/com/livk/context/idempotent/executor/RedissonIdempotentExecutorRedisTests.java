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

package com.livk.context.idempotent.executor;

import com.livk.context.idempotent.exception.IdempotentException;
import com.livk.testcontainers.DockerImageNames;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration tests for {@link RedissonIdempotentExecutor} against a real Redis instance.
 *
 * @author livk
 */
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class RedissonIdempotentExecutorRedisTests {

	@Container
	static final RedisContainer redis = new RedisContainer(DockerImageNames.redis());

	static RedissonClient redissonClient;

	static RedissonIdempotentExecutor executor;

	@BeforeAll
	static void setUp() {
		redis.start();
		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + redis.getHost() + ":" + redis.getFirstMappedPort());
		redissonClient = Redisson.create(config);
		executor = new RedissonIdempotentExecutor(redissonClient);
	}

	@AfterAll
	static void close() {
		if (redissonClient != null) {
			redissonClient.shutdown();
		}
		redis.stop();
	}

	@Test
	void tryAcquireOccupiesKeyUntilReleased() {
		String key = uniqueKey("occupy");
		String token = executor.tryAcquire(key, Duration.ofSeconds(30));

		assertThat(token).isNotBlank();
		assertThat(executor.tryAcquire(key, Duration.ofSeconds(30))).isNull();

		executor.release(key, token);

		assertThat(executor.tryAcquire(key, Duration.ofSeconds(30))).isNotBlank();
	}

	@Test
	void releaseWithWrongTokenKeepsKeyOccupied() {
		String key = uniqueKey("owner");
		String token = executor.tryAcquire(key, Duration.ofSeconds(30));

		assertThat(token).isNotBlank();
		executor.release(key, "not-the-owner");
		assertThat(executor.tryAcquire(key, Duration.ofSeconds(30))).isNull();

		executor.release(key, token);
		assertThat(executor.tryAcquire(key, Duration.ofSeconds(30))).isNotBlank();
	}

	@Test
	void keyBecomesAvailableAfterTtlExpires() throws InterruptedException {
		String key = uniqueKey("ttl");
		assertThat(executor.tryAcquire(key, Duration.ofMillis(200))).isNotBlank();
		assertThat(executor.tryAcquire(key, Duration.ofSeconds(30))).isNull();

		Thread.sleep(400);

		assertThat(executor.tryAcquire(key, Duration.ofSeconds(30))).isNotBlank();
	}

	@Test
	void tryAcquireRejectsBlankKeyAgainstRedis() {
		assertThatThrownBy(() -> executor.tryAcquire(" ", Duration.ofSeconds(1)))
			.isInstanceOf(IdempotentException.class);
	}

	private static String uniqueKey(String prefix) {
		return prefix + ":" + UUID.randomUUID();
	}

}
