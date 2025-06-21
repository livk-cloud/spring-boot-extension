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

package com.livk.autoconfigure.redisearch;

import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.cluster.RedisModulesClusterClient;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisCredentials;
import io.lettuce.core.RedisURI;
import io.lettuce.core.StaticCredentialsProvider;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.resource.ClientResources;
import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.PropertyMapper;

import java.util.List;

/**
 * @author livk
 */
@RequiredArgsConstructor
class RedisModulesFactory {

	private static final String REDIS_PROTOCOL_PREFIX = "redis://";

	private static final String REDISS_PROTOCOL_PREFIX = "rediss://";

	private final RediSearchProperties properties;

	private final PropertyMapper mapper = PropertyMapper.get().alwaysApplyingWhenNonNull();

	public final AbstractRedisClient getClient(ClientResources clientResources) {
		if (properties.getCluster() != null && properties.getCluster().getEnabled()) {
			List<RedisURI> clusterNodes = properties.getCluster()
				.getNodes()
				.stream()
				.map(this::createRedisURI)
				.toList();
			RedisModulesClusterClient clusterClient = RedisModulesClusterClient.create(clientResources, clusterNodes);
			ClusterClientOptions.Builder builder = ((ClusterClientOptions) clusterClient.getOptions()).mutate();
			mapper.from(properties::getCluster)
				.as(RediSearchProperties.Cluster::getMaxRedirects)
				.to(builder::maxRedirects);
			clusterClient.setOptions(builder.build());
			return clusterClient;
		}
		else {
			RedisURI standaloneNode = createRedisURI(properties.getHost() + ":" + properties.getPort());
			return RedisModulesClient.create(clientResources, standaloneNode);
		}
	}

	public RedisURI createRedisURI(String node) {
		String uri = (properties.getSsl() ? REDISS_PROTOCOL_PREFIX : REDIS_PROTOCOL_PREFIX) + node;
		RedisURI redisURI = RedisURI.create(uri);
		RedisCredentials credentials = RedisCredentials.just(properties.getUsername(), properties.getPassword());
		mapper.from(credentials).as(StaticCredentialsProvider::new).to(redisURI::setCredentialsProvider);
		mapper.from(properties::getDatabase).to(redisURI::setDatabase);
		mapper.from(properties::getTimeout).to(redisURI::setTimeout);
		mapper.from(properties::getSsl).to(redisURI::setSsl);
		mapper.from(properties::getClientName).to(redisURI::setClientName);
		return redisURI;
	}

	public <T> GenericObjectPoolConfig<T> getPoolConfig() {
		GenericObjectPoolConfig<T> config = new GenericObjectPoolConfig<>();
		config.setJmxEnabled(false);
		RediSearchProperties.Pool pool = properties.getPool();
		mapper.from(pool::getMaxActive).to(config::setMaxTotal);
		mapper.from(pool::getMaxIdle).to(config::setMaxIdle);
		mapper.from(pool::getMinIdle).to(config::setMinIdle);
		mapper.from(pool::getMaxWait).to(config::setMaxWait);
		return config;
	}

}
