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

import com.livk.testcontainers.DockerImageNames;
import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.testcontainers.RedisStackContainer;
import io.lettuce.core.RedisURI;
import io.lettuce.core.resource.ClientResources;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class FactoryProxySupportTest {

	@Container
	static final RedisStackContainer redisStack = new RedisStackContainer(DockerImageNames.redisStack());

	static RediSearchConnectionFactory factory;

	@BeforeAll
	static void beforeAll() {
		redisStack.start();
		ClientResources resources = ClientResources.builder().build();
		RedisModulesClient client = RedisModulesClient.create(resources,
				RedisURI.create(redisStack.getHost(), redisStack.getFirstMappedPort()));
		factory = FactoryProxySupport.newProxy(client);
	}

	@AfterAll
	static void afterAll() {
		factory.close();
		redisStack.stop();
	}

	@Test
	void newProxy() {
		try (StatefulRedisModulesConnection<String, String> connect = factory.connect()) {
			assertEquals("PONG", connect.sync().ping());
		}
	}

}
