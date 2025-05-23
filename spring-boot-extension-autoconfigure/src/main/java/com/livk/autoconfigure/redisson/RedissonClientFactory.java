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

package com.livk.autoconfigure.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The type Redisson client factory.
 *
 * @author livk
 */
abstract class RedissonClientFactory {

	private static final String REDIS_PROTOCOL_PREFIX = "redis://";

	private static final String REDISS_PROTOCOL_PREFIX = "rediss://";

	/**
	 * Create redisson client.
	 * @param properties the properties
	 * @param redisProperties the redis properties
	 * @param configCustomizers the config customizers
	 * @return the redisson client
	 */
	public static RedissonClient create(ConfigProperties properties, RedisProperties redisProperties,
			ObjectProvider<ConfigCustomizer> configCustomizers) {
		Config config = Optional.<Config>ofNullable(properties.getConfig()).orElse(createConfig(redisProperties));
		configCustomizers.orderedStream().forEach(customizer -> customizer.customize(config));
		return Redisson.create(config);
	}

	private static Config createConfig(RedisProperties redisProperties) {
		Config config;
		Duration duration = redisProperties.getTimeout();
		int timeout = duration == null ? 10000 : (int) duration.toMillis();
		if (redisProperties.getSentinel() != null) {
			List<String> nodeList = redisProperties.getSentinel().getNodes();
			String[] nodes = convert(nodeList);
			config = new Config();
			config.useSentinelServers()
				.setMasterName(redisProperties.getSentinel().getMaster())
				.addSentinelAddress(nodes)
				.setDatabase(redisProperties.getDatabase())
				.setConnectTimeout(timeout)
				.setPassword(redisProperties.getPassword());
		}
		else if (redisProperties.getCluster() != null) {
			List<String> nodeList = redisProperties.getCluster().getNodes();
			String[] nodes = convert(nodeList);
			config = new Config();
			config.useClusterServers()
				.addNodeAddress(nodes)
				.setConnectTimeout(timeout)
				.setPassword(redisProperties.getPassword());
		}
		else {
			config = new Config();
			String prefix = REDIS_PROTOCOL_PREFIX;
			if (redisProperties.getSsl().isEnabled()) {
				prefix = REDISS_PROTOCOL_PREFIX;
			}
			config.useSingleServer()
				.setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
				.setConnectTimeout(timeout)
				.setDatabase(redisProperties.getDatabase())
				.setPassword(redisProperties.getPassword());
		}
		return config;
	}

	private static String[] convert(List<String> nodesObject) {
		List<String> nodes = new ArrayList<>(nodesObject.size());
		for (String node : nodesObject) {
			if (!node.startsWith(REDIS_PROTOCOL_PREFIX) && !node.startsWith(REDISS_PROTOCOL_PREFIX)) {
				nodes.add(REDIS_PROTOCOL_PREFIX + node);
			}
			else {
				nodes.add(node);
			}
		}
		return nodes.toArray(new String[0]);
	}

}
