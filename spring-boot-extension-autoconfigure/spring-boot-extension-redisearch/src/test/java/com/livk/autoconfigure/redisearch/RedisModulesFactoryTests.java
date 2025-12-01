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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class RedisModulesFactoryTests {

	final RedisModulesFactory factory = new RedisModulesFactory(new RediSearchProperties());

	@Test
	void getClient() {
		AbstractRedisClient client = factory.getClient(ClientResources.builder().build());
		assertThat(client).isInstanceOf(RedisModulesClient.class);
	}

	@Test
	void createRedisURI() {
		RedisURI uri = factory.createRedisURI("127.0.0.1:6379");
		assertThat(uri.getHost()).isEqualTo("127.0.0.1");
		assertThat(uri.getPort()).isEqualTo(6379);
	}

	@Test
	void getPoolConfig() {
		GenericObjectPoolConfig<?> config = factory.getPoolConfig();
		assertThat(config.getMaxTotal()).isEqualTo(8);
		assertThat(config.getMaxIdle()).isEqualTo(8);
		assertThat(config.getMinIdle()).isEqualTo(0);
	}

}
