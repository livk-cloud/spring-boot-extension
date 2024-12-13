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

import com.livk.context.redisearch.RediSearchConnectionFactory;
import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.cluster.RedisModulesClusterClient;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisCredentials;
import io.lettuce.core.RedisURI;
import io.lettuce.core.StaticCredentialsProvider;
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
public final class LettuceModConnectionFactory implements RediSearchConnectionFactory {

	private static final String REDIS_PROTOCOL_PREFIX = "redis://";

	private static final String REDISS_PROTOCOL_PREFIX = "rediss://";

	@Delegate
	private final RediSearchConnectionFactory factory;

	private final RediSearchProperties properties;

	public LettuceModConnectionFactory(ClientResources resources, RediSearchProperties properties) {
		this.properties = properties;
		this.factory = init(resources);
	}

	public boolean isCluster() {
		return Optional.ofNullable(properties.getCluster()).map(RediSearchProperties.Cluster::getEnabled).orElse(false);
	}

	private RediSearchConnectionFactory init(ClientResources resources) {
		return isCluster() ? createClusterFactory(resources) : createFactory(resources);
	}

	private RediSearchConnectionFactory createFactory(ClientResources clientResources) {
		RedisURI redisURI = createRedisURI(properties.getHost() + ":" + properties.getPort());
		RedisModulesClient client = RedisModulesClient.create(clientResources, redisURI);
		ClientOptions.Builder builder = client.getOptions().mutate();
		client.setOptions(builder.build());
		return RediSearchConnectionFactory.create(client);
	}

	private RediSearchConnectionFactory createClusterFactory(ClientResources clientResources) {
		List<RedisURI> redisURIList = properties.getCluster().getNodes().stream().map(this::createRedisURI).toList();
		RedisModulesClusterClient clusterClient = RedisModulesClusterClient.create(clientResources, redisURIList);
		ClusterClientOptions.Builder builder = ((ClusterClientOptions) clusterClient.getOptions()).mutate();
		if (properties.getCluster().getMaxRedirects() != null) {
			builder.maxRedirects(properties.getCluster().getMaxRedirects());
		}
		clusterClient.setOptions(builder.build());
		return RediSearchConnectionFactory.create(clusterClient);
	}

	private RedisURI createRedisURI(String node) {
		String uri = (properties.getSsl() ? REDISS_PROTOCOL_PREFIX : REDIS_PROTOCOL_PREFIX) + node;
		RedisURI redisURI = RedisURI.create(uri);
		RedisCredentials credentials = RedisCredentials.just(properties.getUsername(), properties.getPassword());
		StaticCredentialsProvider credentialsProvider = new StaticCredentialsProvider(credentials);
		redisURI.setCredentialsProvider(credentialsProvider);
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
	public <T> GenericObjectPoolConfig<T> getPoolConfig() {
		RediSearchProperties.Pool pool = properties.getPool();
		if (pool.getEnabled()) {
			GenericObjectPoolConfig<T> config = new GenericObjectPoolConfig<>();
			config.setJmxEnabled(false);
			config.setMaxTotal(pool.getMaxActive());
			config.setMaxIdle(pool.getMaxIdle());
			config.setMinIdle(pool.getMinIdle());
			if (pool.getMaxWait() != null) {
				config.setMaxWait(pool.getMaxWait());
			}
			return config;
		}
		return RediSearchConnectionFactory.super.getPoolConfig();
	}

}
