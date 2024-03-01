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

package com.livk.core.redis;

import com.livk.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.redis.connection.RedisConnectionCommands;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * RedisOpsTest
 * </p>
 *
 * @author livk
 */
@ContextConfiguration(classes = { RedisFactoryConfig.class })
@ExtendWith(SpringExtension.class)
@Testcontainers(disabledWithoutDocker = true)
class RedisOpsTest {

	@Container
	@ServiceConnection
	static RedisContainer redis = new RedisContainer().withPassword("123456");

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("redis.host", redis::getHost);
		registry.add("redis.port", redis::getFirstMappedPort);
		registry.add("redis.password", redis::getPassword);
	}

	@Autowired
	RedisConnectionFactory connectionFactory;

	@Test
	void test() {
		RedisOps ops = new RedisOps(connectionFactory);
		assertEquals("PONG", ops.execute(RedisConnectionCommands::ping));
	}

}
