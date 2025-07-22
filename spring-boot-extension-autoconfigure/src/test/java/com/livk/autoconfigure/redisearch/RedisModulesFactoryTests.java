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

package com.livk.autoconfigure.redisearch;

import com.redis.lettucemod.RedisModulesClient;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.resource.ClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * @author livk
 */
class RedisModulesFactoryTests {

	final RedisModulesFactory factory = new RedisModulesFactory(new RediSearchProperties());

	@Test
	void getClient() {
		AbstractRedisClient client = factory.getClient(ClientResources.builder().build());
		assertInstanceOf(RedisModulesClient.class, client);
	}

	@Test
	void createRedisURI() {
		RedisURI uri = factory.createRedisURI("127.0.0.1:6379");
		assertEquals("127.0.0.1", uri.getHost());
		assertEquals(6379, uri.getPort());
	}

	@Test
	void getPoolConfig() {
		GenericObjectPoolConfig<?> config = factory.getPoolConfig();
		assertEquals(8, config.getMaxTotal());
		assertEquals(8, config.getMaxIdle());
		assertEquals(0, config.getMinIdle());
	}

}
