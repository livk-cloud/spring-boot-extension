/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.redisearch;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.livk.context.redisearch.codec.RedisCodecs;
import com.livk.testcontainers.containers.RedisContainer;
import io.lettuce.core.codec.RedisCodec;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
@SpringJUnitConfig(RediSearchConfig.class)
@Testcontainers(disabledWithoutDocker = true)
class RediSearchTemplateTest {

	@Container
	@ServiceConnection
	static RedisContainer redisStack = RedisContainer.redisStack();

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("redisearch.host", redisStack::getHost);
		registry.add("redisearch.port", redisStack::getFirstMappedPort);
	}

	@Autowired
	RedisSearchConnectionFactory factory;

	@Test
	void test() throws Exception {
		RedisCodec<String, Object> redisCodec = RedisCodecs.json(new JsonMapper(), String.class, Object.class);
		RediSearchTemplate<String, Object> template = new RediSearchTemplate<>(factory, redisCodec);
		template.afterPropertiesSet();

		assertEquals("PONG", template.async().ping().get());
		assertEquals("PONG", template.reactive().ping().block());
	}

}
