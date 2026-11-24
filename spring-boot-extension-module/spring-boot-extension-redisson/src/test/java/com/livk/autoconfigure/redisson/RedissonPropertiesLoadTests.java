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

package com.livk.autoconfigure.redisson;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class RedissonPropertiesLoadTests {

	@Test
	void loadReturnsDefaultWhenPrefixMissing() {
		RedissonProperties properties = RedissonProperties.load(new MockEnvironment());
		assertThat(properties).isNotNull();
		assertThat(properties.getConfig()).isNotNull();
		assertThat(properties.getConfig().getSingleServerConfig()).isNull();
	}

	@Test
	void loadBindsSentinelServersConfig() {
		MockEnvironment environment = new MockEnvironment()
			.withProperty("spring.redisson.config.sentinel-servers-config.master-name", "mymaster")
			.withProperty("spring.redisson.config.sentinel-servers-config.sentinel-addresses[0]",
					"redis://127.0.0.1:26379");

		RedissonProperties properties = RedissonProperties.load(environment);

		assertThat(properties.getConfig().getSentinelServersConfig()).isNotNull();
		assertThat(properties.getConfig().getSentinelServersConfig().getMasterName()).isEqualTo("mymaster");
		assertThat(properties.getConfig().getSentinelServersConfig().getSentinelAddresses())
			.containsExactly("redis://127.0.0.1:26379");
	}

	@Test
	void loadBindsClusterServersConfig() {
		MockEnvironment environment = new MockEnvironment()
			.withProperty("spring.redisson.config.cluster-servers-config.node-addresses[0]", "redis://127.0.0.1:7000");

		RedissonProperties properties = RedissonProperties.load(environment);

		assertThat(properties.getConfig().getClusterServersConfig()).isNotNull();
		assertThat(properties.getConfig().getClusterServersConfig().getNodeAddresses())
			.containsExactly("redis://127.0.0.1:7000");
	}

}
