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

package com.livk.context.sequence.builder;

import com.livk.context.sequence.Sequence;
import com.livk.testcontainers.DockerImageNames;
import com.redis.testcontainers.RedisContainer;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers(disabledWithoutDocker = true, parallel = true)
class RedisSequenceBuilderTest {

	@Container
	static final RedisContainer redis = new RedisContainer(DockerImageNames.redis());

	static RedisClient redisClient;

	@BeforeAll
	static void setUp() {
		redis.start();
		redisClient = RedisClient.create("redis://" + redis.getHost() + ":" + redis.getFirstMappedPort());
	}

	@AfterAll
	static void tearDown() {
		redis.stop();
		if (redisClient != null) {
			redisClient.close();
		}
	}

	@Test
	void testBuildSequenceWithRequiredParameters() {
		RedisSequenceBuilder builder = new RedisSequenceBuilder(redisClient).bizName("test-sequence");
		Sequence sequence = builder.build();
		assertNotNull(sequence);
		long first = sequence.nextValue();
		long second = sequence.nextValue();
		assertEquals(1, first);
		assertEquals(2, second);
	}

	@Test
	void testBuildSequenceWithOptionalParameters() {
		RedisSequenceBuilder builder = new RedisSequenceBuilder(redisClient).bizName("opt-sequence")
			.step(10)
			.stepStart(100);
		Sequence sequence = builder.build();
		assertNotNull(sequence);
		long first = sequence.nextValue();
		long second = sequence.nextValue();
		assertEquals(101, first);
		assertEquals(102, second);
	}

	@Test
	void testBuilderMethodChaining() {
		Sequence sequence = new RedisSequenceBuilder(redisClient).step(5)
			.bizName("chain-sequence")
			.stepStart(50)
			.build();
		assertNotNull(sequence);
		assertEquals(51, sequence.nextValue());
		assertEquals(52, sequence.nextValue());
	}

	@Test
	void testBuildSequenceWithRedisConnectionFailure() {
		// Use an unreachable port to simulate connection failure
		int unreachablePort = 6399;
		RedisSequenceBuilder builder = new RedisSequenceBuilder(
				RedisClient.create("redis://localhost:" + unreachablePort))
			.bizName("fail-sequence");
		Exception exception = assertThrows(RedisConnectionException.class, builder::build);
		assertTrue(exception.getMessage().contains("Unable to connect to localhost/<unresolved>:6399"));
	}

	@Test
	void testBuildSequenceWithInvalidStepOrStepStart() {
		// step = 0 should fallback to default (1000)
		Sequence zeroStepSequence = new RedisSequenceBuilder(redisClient).bizName("zero-step").step(0).build();
		assertNotNull(zeroStepSequence);
		assertEquals(1, zeroStepSequence.nextValue());

		// step < 0 should fallback to default (1000)
		Sequence negativeStepSequence = new RedisSequenceBuilder(redisClient).bizName("negative-step")
			.step(-10)
			.build();
		assertNotNull(negativeStepSequence);
		assertEquals(1, negativeStepSequence.nextValue());

		// stepStart < 0 should fallback to default (0)
		Sequence negativeStepStartSequence = new RedisSequenceBuilder(redisClient).bizName("negative-stepstart")
			.stepStart(-100)
			.build();
		assertNotNull(negativeStepStartSequence);
		assertEquals(1, negativeStepStartSequence.nextValue());
	}

}
