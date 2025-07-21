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
import com.livk.testcontainers.DockerImageNames;
import com.redis.testcontainers.RedisContainer;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers(disabledWithoutDocker = true, parallel = true)
class RedisRangeManagerTest {

	@Container
	static final RedisContainer redis = new RedisContainer(DockerImageNames.redis());

	static RedisClient redisClient;

	@BeforeAll
	static void setUp() {
		redis.start();
		redisClient = RedisClient.create("redis://" + redis.getHost() + ":" + redis.getFirstMappedPort());
	}

	@AfterAll
	static void close() {
		if (redisClient != null) {
			redisClient.close();
		}
	}

	@AfterAll
	static void tearDown() {
		redis.stop();
	}

	@Test
	void testInitWithValidConfiguration() {
		RedisRangeManager manager = new RedisRangeManager(redisClient);
		assertDoesNotThrow(manager::init);
	}

	@Test
	void testNextRangeReturnsValidSequenceRangeForNewKey() {
		RedisRangeManager manager = new RedisRangeManager(redisClient);
		manager.init();
		manager.step(10);
		manager.stepStart(100);
		String key = "test-sequence";
		SequenceRange range = manager.nextRange(key);
		assertNotNull(range);
		assertEquals(101, range.getMin());
		assertEquals(110, range.getMax());
	}

	@Test
	void testNextRangeThrowsWhenRedisUnavailable() {
		// Use an unreachable port to simulate connection failure
		int unreachablePort = 6399;
		RedisRangeManager manager = new RedisRangeManager(RedisClient.create("redis://localhost:" + unreachablePort));
		Exception exception = assertThrows(RedisConnectionException.class, manager::init);
		assertTrue(exception.getMessage().contains("Unable to connect to localhost/<unresolved>:6399"));
	}

	@Test
	void testNextRangeDoesNotReinitializeExistingKey() {
		RedisRangeManager manager = new RedisRangeManager(redisClient);
		manager.init();
		manager.step(5);
		manager.stepStart(0);
		String key = "repeat-key";
		SequenceRange firstRange = manager.nextRange(key);
		assertNotNull(firstRange);
		long max1 = firstRange.getMax();

		// The keyAlreadyExist should now be true, so setIfAbsent should not be called
		// again.
		SequenceRange secondRange = manager.nextRange(key);
		assertNotNull(secondRange);
		long min2 = secondRange.getMin();
		long max2 = secondRange.getMax();

		assertEquals(max1 + 1, min2);
		assertEquals(max1 + manager.step, max2);
	}

}
