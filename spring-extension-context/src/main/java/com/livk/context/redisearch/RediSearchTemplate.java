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

package com.livk.context.redisearch;

import com.livk.context.redisearch.codec.JdkRedisCodec;
import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.cluster.RedisModulesClusterClient;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Delegate;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.function.Supplier;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class RediSearchTemplate<K, V>
		implements StatefulRedisModulesConnection<K, V>, InitializingBean, DisposableBean {

	private GenericObjectPool<StatefulRedisModulesConnection<K, V>> genericObjectPool;

	@Setter
	private RedisCodec<K, V> redisCodec;

	private final AbstractRedisClient client;

	private final GenericObjectPoolConfig<StatefulRedisModulesConnection<K, V>> poolConfig;

	@Override
	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {
		if (redisCodec == null) {
			redisCodec = (RedisCodec<K, V>) new JdkRedisCodec();
		}
		if (client instanceof RedisModulesClient redisModulesClient) {
			genericObjectPool = createPool(() -> redisModulesClient.connect(redisCodec));
		}
		else if (client instanceof RedisModulesClusterClient redisModulesClusterClient) {
			genericObjectPool = createPool(() -> redisModulesClusterClient.connect(redisCodec));
		}
		else {
			throw new IllegalArgumentException("client must be RedisModulesClient or RedisModulesClusterClient");
		}
	}

	protected GenericObjectPool<StatefulRedisModulesConnection<K, V>> createPool(
			Supplier<StatefulRedisModulesConnection<K, V>> supplier) {
		return ConnectionPoolSupport.createGenericObjectPool(supplier, poolConfig);
	}

	@Delegate
	@SneakyThrows
	private StatefulRedisModulesConnection<K, V> delegate() {
		return genericObjectPool.borrowObject();
	}

	@Override
	public void destroy() {
		genericObjectPool.close();
		client.shutdown();
	}

}
