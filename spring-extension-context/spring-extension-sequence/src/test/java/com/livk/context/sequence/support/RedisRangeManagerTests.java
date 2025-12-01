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

import com.livk.context.sequence.SequenceRange;
import com.livk.context.sequence.support.redis.LettuceSequenceRedisHelper;
import com.livk.context.sequence.support.redis.SequenceRedisHelper;
import com.livk.testcontainers.DockerImageNames;
import com.redis.testcontainers.RedisContainer;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers(disabledWithoutDocker = true, parallel = true)
class RedisRangeManagerTests {

	@Container
	static final RedisContainer redis = new RedisContainer(DockerImageNames.redis());

	static RedisClient redisClient;

	static SequenceRedisHelper helper;

	@BeforeAll
	static void setUp() {
		redis.start();
		redisClient = RedisClient.create("redis://" + redis.getHost() + ":" + redis.getFirstMappedPort());
		helper = new LettuceSequenceRedisHelper(redisClient);
	}

	@AfterAll
	static void close() {
		if (helper != null) {
			helper.close();
		}
		if (redisClient != null) {
			redisClient.close();
		}
		redis.stop();
	}

	@Test
	void testInitWithValidConfiguration() {
		assertThatCode(() -> new RedisRangeManager(helper)).doesNotThrowAnyException();
	}

	@Test
	void testNextRangeReturnsValidSequenceRangeForNewKey() {
		RedisRangeManager manager = new RedisRangeManager(helper);
		manager.step(10);
		manager.stepStart(100);
		String key = "test-sequence";
		SequenceRange range = manager.nextRange(key);

		assertThat(range).isNotNull();
		assertThat(range.getMin()).isEqualTo(101);
		assertThat(range.getMax()).isEqualTo(110);
	}

	@Test
	void testNextRangeThrowsWhenRedisUnavailable() {
		int unreachablePort = 6399;

		assertThatThrownBy(
				() -> new LettuceSequenceRedisHelper(RedisClient.create("redis://localhost:" + unreachablePort)))
			.isInstanceOf(RedisConnectionException.class)
			.hasMessageContaining("Unable to connect to localhost")
			.hasMessageContaining(":6399");
	}

	@Test
	void testNextRangeDoesNotReinitializeExistingKey() {
		RedisRangeManager manager = new RedisRangeManager(helper);
		manager.step(5);
		manager.stepStart(0);
		String key = "repeat-key";

		SequenceRange firstRange = manager.nextRange(key);
		assertThat(firstRange).isNotNull();
		long max1 = firstRange.getMax();

		SequenceRange secondRange = manager.nextRange(key);
		assertThat(secondRange).isNotNull();
		long min2 = secondRange.getMin();
		long max2 = secondRange.getMax();

		assertThat(min2).isEqualTo(max1 + 1);
		assertThat(max2).isEqualTo(max1 + manager.step);
	}

}
