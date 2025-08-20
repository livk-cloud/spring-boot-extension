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
import com.livk.context.sequence.support.DbRangeManager;
import com.livk.context.sequence.support.RedisRangeManager;
import com.livk.context.sequence.support.redis.LettuceSequenceRedisHelper;
import com.livk.testcontainers.DockerImageNames;
import com.redis.testcontainers.RedisContainer;
import com.zaxxer.hikari.HikariDataSource;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import org.h2.Driver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author livk
 */
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class DefaultSequenceBuilderTests {

	static HikariDataSource dataSource;

	static DbRangeManager dbRangeManager;

	@Container
	static final RedisContainer redis = new RedisContainer(DockerImageNames.redis());

	static RedisClient redisClient;

	static RedisRangeManager redisRangeManager;

	@BeforeAll
	static void setupDataSource() {
		dataSource = new HikariDataSource();
		dataSource.setDriverClassName(Driver.class.getName());
		dataSource.setJdbcUrl("jdbc:h2:mem:test");
		dbRangeManager = new DbRangeManager(dataSource);

		redis.start();
		redisClient = RedisClient.create("redis://" + redis.getHost() + ":" + redis.getFirstMappedPort());

		LettuceSequenceRedisHelper helper = new LettuceSequenceRedisHelper(redisClient);
		redisRangeManager = new RedisRangeManager(helper);
	}

	@AfterAll
	static void closeDataSource() {
		if (dataSource != null) {
			dataSource.close();
		}
		if (redisClient != null) {
			redisClient.close();
		}
		redis.stop();
	}

	@Test
	void testBuildDbSequenceWithAllRequiredParameters() {
		SequenceBuilder builder = SequenceBuilder.builder(dbRangeManager).bizName("test-biz");
		Sequence sequence = builder.build();
		assertThat(sequence).isNotNull();
		for (int i = 0; i < 10; i++) {
			assertThat(sequence.nextValue()).isEqualTo(i + 1);
		}
	}

	@Test
	void testCustomStepValueApplied() throws Exception {
		int customStep = 123;
		SequenceBuilder builder = SequenceBuilder.builder(dbRangeManager).bizName("step-biz").step(customStep);
		Sequence sequence = builder.build();
		assertThat(sequence).isNotNull();

		// Use reflection to access the step value in DbRangeManager
		Field managerField = sequence.getClass().getDeclaredField("manager");
		managerField.setAccessible(true);
		Object manager = managerField.get(sequence);
		Field stepField = DbRangeManager.class.getSuperclass().getDeclaredField("step");
		stepField.setAccessible(true);
		int actualStep = (int) stepField.get(manager);
		assertThat(actualStep).isEqualTo(customStep);
	}

	@Test
	void testCustomStepStartValueApplied() throws Exception {
		long customStepStart = 55L;
		SequenceBuilder builder = SequenceBuilder.builder(dbRangeManager)
			.bizName("stepStart-biz")
			.stepStart(customStepStart);
		Sequence sequence = builder.build();
		assertThat(sequence).isNotNull();

		// Use reflection to access the stepStart value in DbRangeManager
		Field managerField = sequence.getClass().getDeclaredField("manager");
		managerField.setAccessible(true);
		Object manager = managerField.get(sequence);
		Field stepStartField = DbRangeManager.class.getSuperclass().getDeclaredField("stepStart");
		stepStartField.setAccessible(true);
		long actualStepStart = (long) stepStartField.get(manager);
		assertThat(actualStepStart).isEqualTo(customStepStart);
	}

	@Test
	void testBuildThrowsExceptionWhenBizNameMissing() {
		SequenceBuilder builder = SequenceBuilder.builder(dbRangeManager);
		Throwable exception = catchThrowable(builder::build);
		assertThat(exception).isInstanceOf(IllegalArgumentException.class);
		assertThat(exception.getMessage()).contains("name is required");
	}

	@Test
	void testNegativeStepValueIgnored() throws Exception {
		SequenceBuilder builder = SequenceBuilder.builder(dbRangeManager).bizName("neg-step-biz").step(-100);
		Sequence sequence = builder.build();
		assertThat(sequence).isNotNull();

		// Should still use default step (1000)
		Field managerField = sequence.getClass().getDeclaredField("manager");
		managerField.setAccessible(true);
		Object manager = managerField.get(sequence);
		Field stepField = DbRangeManager.class.getSuperclass().getDeclaredField("step");
		stepField.setAccessible(true);
		int actualStep = (int) stepField.get(manager);
		assertThat(actualStep).isEqualTo(10);
	}

	@Test
	void testNegativeStepStartValueIgnored() throws Exception {
		SequenceBuilder builder = SequenceBuilder.builder(dbRangeManager).bizName("neg-stepStart-biz").stepStart(-99L);
		Sequence sequence = builder.build();
		assertThat(sequence).isNotNull();

		// Should still use default stepStart (0)
		Field managerField = sequence.getClass().getDeclaredField("manager");
		managerField.setAccessible(true);
		Object manager = managerField.get(sequence);
		Field stepStartField = DbRangeManager.class.getSuperclass().getDeclaredField("stepStart");
		stepStartField.setAccessible(true);
		long actualStepStart = (long) stepStartField.get(manager);
		assertThat(actualStepStart).isEqualTo(0);
	}

	@Test
	void testBuildSequenceWithOptionalParameters() {
		SequenceBuilder builder = SequenceBuilder.builder(dbRangeManager)
			.bizName("opt-sequence")
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
		Sequence sequence = SequenceBuilder.builder(dbRangeManager)
			.step(5)
			.bizName("chain-sequence")
			.stepStart(50)
			.build();
		assertThat(sequence).isNotNull();
		for (int i = 0; i < 10; i++) {
			assertThat(sequence.nextValue()).isEqualTo(i + 51);
		}
	}

	@Test
	void testBuildRedisSequenceWithAllRequiredParameters() {
		SequenceBuilder builder = SequenceBuilder.builder(redisRangeManager).bizName("test-sequence");
		Sequence sequence = builder.build();
		assertThat(sequence).isNotNull();
		for (int i = 0; i < 10; i++) {
			assertThat(sequence.nextValue()).isEqualTo(i + 1);
		}
	}

	@Test
	void testBuildSequenceWithRedisConnectionFailure() {
		// Use an unreachable port to simulate connection failure
		int unreachablePort = 6399;
		Throwable exception = catchThrowable(
				() -> new LettuceSequenceRedisHelper(RedisClient.create("redis://localhost:" + unreachablePort)));
		assertThat(exception).isInstanceOf(RedisConnectionException.class);
		assertThat(exception.getMessage()).contains("Unable to connect to localhost/<unresolved>:6399");
	}

	@Test
	void testBuildSequenceWithInvalidStepOrStepStart() {
		// step = 0 should fallback to default (1000)
		Sequence zeroStepSequence = SequenceBuilder.builder(dbRangeManager).bizName("zero-step").step(0).build();
		assertThat(zeroStepSequence).isNotNull();
		for (int i = 0; i < 10; i++) {
			assertThat(zeroStepSequence.nextValue()).isEqualTo(i + 1);
		}

		// step < 0 should fallback to default (1000)
		Sequence negativeStepSequence = SequenceBuilder.builder(dbRangeManager)
			.bizName("negative-step")
			.step(-10)
			.build();
		assertThat(negativeStepSequence).isNotNull();
		for (int i = 0; i < 10; i++) {
			assertThat(negativeStepSequence.nextValue()).isEqualTo(i + 1);
		}

		// stepStart < 0 should fallback to default (0)
		Sequence negativeStepStartSequence = SequenceBuilder.builder(dbRangeManager)
			.bizName("negative-step-start")
			.stepStart(-100)
			.build();
		assertThat(negativeStepStartSequence).isNotNull();
		for (int i = 0; i < 10; i++) {
			assertThat(negativeStepStartSequence.nextValue()).isEqualTo(i + 1);
		}
	}

}
