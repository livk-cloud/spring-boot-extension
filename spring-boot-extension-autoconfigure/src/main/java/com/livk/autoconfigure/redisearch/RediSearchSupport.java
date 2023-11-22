/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.autoconfigure.redisearch;

import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.cluster.RedisModulesClusterClient;
import io.lettuce.core.RedisCredentials;
import io.lettuce.core.RedisCredentialsProvider;
import io.lettuce.core.RedisURI;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.experimental.UtilityClass;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

/**
 * The type Redi search support.
 *
 * @author livk
 */
@UtilityClass
final class RediSearchSupport {

	/**
	 * Create cluster list.
	 * @param properties the properties
	 * @return the list
	 */
	public List<RedisURI> createCluster(RediSearchProperties properties) {
		return properties.getCluster().getNodes().stream().map(node -> {
			String[] arr = node.split(":");
			RedisURI redisURI = RedisURI.create(arr[0], Integer.parseInt(arr[1]));
			config(redisURI, properties);
			return redisURI;
		}).toList();
	}

	/**
	 * Create redis uri.
	 * @param properties the properties
	 * @return the redis uri
	 */
	public RedisURI create(RediSearchProperties properties) {
		RedisURI redisURI = RedisURI.create(properties.getHost(), properties.getPort());
		config(redisURI, properties);
		return redisURI;
	}

	private void config(RedisURI redisURI, RediSearchProperties properties) {
		redisURI.setCredentialsProvider(RedisCredentialsProvider
			.from(() -> RedisCredentials.just(properties.getUsername(), properties.getPassword())));
		redisURI.setDatabase(properties.getDatabase());
		Duration timeout = properties.getTimeout();
		if (timeout != null) {
			redisURI.setTimeout(timeout);
		}
		redisURI.setSsl(properties.getSsl());
		redisURI.setClientName(properties.getClientName());
	}

	/**
	 * With pool config generic object pool config.
	 * @param <T> the type parameter
	 * @param properties the properties
	 * @return the generic object pool config
	 */
	public static <T> GenericObjectPoolConfig<T> withPoolConfig(RediSearchProperties properties) {
		GenericObjectPoolConfig<T> config = new GenericObjectPoolConfig<>();
		config.setJmxEnabled(false);
		RediSearchProperties.Pool pool = properties.getPool();
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

	/**
	 * Pool generic object pool.
	 * @param <K> the type parameter
	 * @param <V> the type parameter
	 * @param supplier the supplier
	 * @param poolConfig the pool config
	 * @return the generic object pool
	 */
	public <K, V> GenericObjectPool<StatefulRedisModulesConnection<K, V>> pool(
			Supplier<StatefulRedisModulesConnection<K, V>> supplier,
			GenericObjectPoolConfig<StatefulRedisModulesConnection<K, V>> poolConfig) {
		return ConnectionPoolSupport.createGenericObjectPool(supplier, poolConfig);
	}

	/**
	 * Pool generic object pool.
	 * @param <K> the type parameter
	 * @param <V> the type parameter
	 * @param client the client
	 * @param redisCodec the redis codec
	 * @param poolConfig the pool config
	 * @return the generic object pool
	 */
	public <K, V> GenericObjectPool<StatefulRedisModulesConnection<K, V>> pool(RedisModulesClient client,
			RedisCodec<K, V> redisCodec, GenericObjectPoolConfig<StatefulRedisModulesConnection<K, V>> poolConfig) {
		return pool(() -> client.connect(redisCodec), poolConfig);
	}

	/**
	 * Pool generic object pool.
	 * @param <K> the type parameter
	 * @param <V> the type parameter
	 * @param client the client
	 * @param redisCodec the redis codec
	 * @param poolConfig the pool config
	 * @return the generic object pool
	 */
	public <K, V> GenericObjectPool<StatefulRedisModulesConnection<K, V>> pool(RedisModulesClusterClient client,
			RedisCodec<K, V> redisCodec, GenericObjectPoolConfig<StatefulRedisModulesConnection<K, V>> poolConfig) {
		return pool(() -> client.connect(redisCodec), poolConfig);
	}

}
