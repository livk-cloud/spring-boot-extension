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

package com.livk.context.sequence.support.redis;

import com.livk.testcontainers.DockerImageNames;
import com.redis.testcontainers.RedisContainer;
import io.lettuce.core.RedisClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class LettuceSequenceRedisHelperTests {

	@Container
	static final RedisContainer redis = new RedisContainer(DockerImageNames.redis());

	static RedisClient redisClient;

	static LettuceSequenceRedisHelper helper;

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
	void test() {
		byte[] key = "test".getBytes();
		helper.setNx(key, 1L);

		assertThat(helper.incrBy(key, 100)).isEqualTo(101);
	}

}
