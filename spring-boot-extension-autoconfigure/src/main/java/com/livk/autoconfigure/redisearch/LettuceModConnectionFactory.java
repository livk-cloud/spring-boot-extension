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

package com.livk.autoconfigure.redisearch;

import com.livk.context.redisearch.RedisModulesClusterConnectionFactory;
import com.livk.context.redisearch.RedisModulesConnectionFactory;
import com.livk.context.redisearch.RedisSearchConnectionFactory;
import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.cluster.RedisModulesClusterClient;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisCredentials;
import io.lettuce.core.RedisCredentialsProvider;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.resource.ClientResources;
import lombok.experimental.Delegate;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * @author livk
 */
public class LettuceModConnectionFactory implements RedisSearchConnectionFactory {

	private static final String REDIS_PROTOCOL_PREFIX = "redis://";

	private static final String REDISS_PROTOCOL_PREFIX = "rediss://";

	@Delegate
	private final RedisSearchConnectionFactory factory;

	private final boolean isCluster;

	private final RediSearchProperties.Pool pool;

	public LettuceModConnectionFactory(ClientResources resources, RediSearchProperties properties) {
		this.isCluster = Optional.ofNullable(properties.getCluster())
			.map(RediSearchProperties.Cluster::getEnabled)
			.orElse(false);
		this.factory = init(resources, properties);
		this.pool = properties.getPool();
	}

	private RedisSearchConnectionFactory init(ClientResources resources, RediSearchProperties properties) {
		return isCluster ? createClusterFactory(resources, properties) : createFactory(resources, properties);
	}

	private RedisSearchConnectionFactory createFactory(ClientResources clientResources,
			RediSearchProperties properties) {
		RedisURI redisURI = createRedisURI(properties.getHost() + ":" + properties.getPort(), properties);
		RedisModulesClient client = RedisModulesClient.create(clientResources, redisURI);
		ClientOptions.Builder builder = client.getOptions().mutate();
		client.setOptions(builder.build());
		return new RedisModulesConnectionFactory(client);
	}

	private RedisSearchConnectionFactory createClusterFactory(ClientResources clientResources,
			RediSearchProperties properties) {
		List<RedisURI> redisURIList = properties.getCluster()
			.getNodes()
			.stream()
			.map(node -> createRedisURI(node, properties))
			.toList();
		RedisModulesClusterClient clusterClient = RedisModulesClusterClient.create(clientResources, redisURIList);
		ClusterClientOptions.Builder builder = ((ClusterClientOptions) clusterClient.getOptions()).mutate();
		if (properties.getCluster().getMaxRedirects() != null) {
			builder.maxRedirects(properties.getCluster().getMaxRedirects());
		}
		clusterClient.setOptions(builder.build());
		return new RedisModulesClusterConnectionFactory(clusterClient);
	}

	private RedisURI createRedisURI(String node, RediSearchProperties properties) {
		String uri = (properties.getSsl() ? REDISS_PROTOCOL_PREFIX : REDIS_PROTOCOL_PREFIX) + node;
		RedisURI redisURI = RedisURI.create(uri);
		redisURI.setCredentialsProvider(RedisCredentialsProvider
			.from(() -> RedisCredentials.just(properties.getUsername(), properties.getPassword())));
		redisURI.setDatabase(properties.getDatabase());
		Duration timeout = properties.getTimeout();
		if (timeout != null) {
			redisURI.setTimeout(timeout);
		}
		redisURI.setSsl(properties.getSsl());
		redisURI.setClientName(properties.getClientName());
		return redisURI;
	}

	@Override
	public final <T> GenericObjectPoolConfig<T> getPoolConfig() {
		GenericObjectPoolConfig<T> config = new GenericObjectPoolConfig<>();
		config.setJmxEnabled(false);
		if (pool != null) {
			config.setMaxTotal(pool.getMaxActive());
			config.setMaxIdle(pool.getMaxIdle());
			config.setMinIdle(pool.getMinIdle());
			if (pool.getMaxWait() != null) {
				config.setMaxWait(pool.getMaxWait());
			}
		}
		return config;
	}

}
