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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@Testcontainers(disabledWithoutDocker = true, parallel = true)
class RedisSequenceBuilderTests {

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
		assertThat(sequence).isNotNull();
		for (int i = 0; i < 10; i++) {
			assertThat(sequence.nextValue()).isEqualTo(i + 1);
		}
	}

	@Test
	void testBuildSequenceWithOptionalParameters() {
		RedisSequenceBuilder builder = new RedisSequenceBuilder(redisClient).bizName("opt-sequence")
			.step(10)
			.stepStart(100);
		Sequence sequence = builder.build();
		assertThat(sequence).isNotNull();
		for (int i = 0; i < 10; i++) {
			assertThat(sequence.nextValue()).isEqualTo(i + 101);
		}
	}

	@Test
	void testBuilderMethodChaining() {
		Sequence sequence = new RedisSequenceBuilder(redisClient).step(5)
			.bizName("chain-sequence")
			.stepStart(50)
			.build();
		assertThat(sequence).isNotNull();
		for (int i = 0; i < 10; i++) {
			assertThat(sequence.nextValue()).isEqualTo(i + 51);
		}
	}

	@Test
	void testBuildSequenceWithRedisConnectionFailure() {
		// Use an unreachable port to simulate connection failure
		int unreachablePort = 6399;
		RedisSequenceBuilder builder = new RedisSequenceBuilder(
				RedisClient.create("redis://localhost:" + unreachablePort))
			.bizName("fail-sequence");
		Throwable exception = catchThrowable(builder::build);
		assertThat(exception).isInstanceOf(RedisConnectionException.class);
		assertThat(exception.getMessage()).contains("Unable to connect to localhost/<unresolved>:6399");
	}

	@Test
	void testBuildSequenceWithInvalidStepOrStepStart() {
		// step = 0 should fallback to default (1000)
		Sequence zeroStepSequence = new RedisSequenceBuilder(redisClient).bizName("zero-step").step(0).build();
		assertThat(zeroStepSequence).isNotNull();
		for (int i = 0; i < 10; i++) {
			assertThat(zeroStepSequence.nextValue()).isEqualTo(i + 1);
		}

		// step < 0 should fallback to default (1000)
		Sequence negativeStepSequence = new RedisSequenceBuilder(redisClient).bizName("negative-step")
			.step(-10)
			.build();
		assertThat(negativeStepSequence).isNotNull();
		for (int i = 0; i < 10; i++) {
			assertThat(negativeStepSequence.nextValue()).isEqualTo(i + 1);
		}

		// stepStart < 0 should fallback to default (0)
		Sequence negativeStepStartSequence = new RedisSequenceBuilder(redisClient).bizName("negative-stepstart")
			.stepStart(-100)
			.build();
		assertThat(negativeStepStartSequence).isNotNull();
		for (int i = 0; i < 10; i++) {
			assertThat(negativeStepStartSequence.nextValue()).isEqualTo(i + 1);
		}
	}

}
