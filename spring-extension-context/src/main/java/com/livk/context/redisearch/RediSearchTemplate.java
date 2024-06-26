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
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.SneakyThrows;
import lombok.experimental.Delegate;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author livk
 */
public class RediSearchTemplate<K, V> implements StatefulRedisModulesConnection<K, V> {

	private final GenericObjectPool<StatefulRedisModulesConnection<K, V>> delegate;

	private final RedisSearchConnectionFactory factory;

	@SuppressWarnings("unchecked")
	public RediSearchTemplate(RedisSearchConnectionFactory factory) {
		this(factory, (RedisCodec<K, V>) new JdkRedisCodec());
	}

	public RediSearchTemplate(RedisSearchConnectionFactory factory, RedisCodec<K, V> redisCodec) {
		this.factory = factory;
		this.delegate = ConnectionPoolSupport.createGenericObjectPool(() -> factory.connect(redisCodec),
				getPoolConfig());
	}

	@SneakyThrows
	@Delegate
	protected StatefulRedisModulesConnection<K, V> borrowObject() {
		return delegate.borrowObject();
	}

	protected GenericObjectPoolConfig<StatefulRedisModulesConnection<K, V>> getPoolConfig() {
		return factory.getPoolConfig();
	}

}
