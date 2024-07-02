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

import com.livk.context.redisearch.codec.RedisCodecs;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Delegate;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.function.Supplier;

/**
 * @author livk
 */
public class RediSearchTemplate<K, V> implements StatefulRedisModulesConnection<K, V>, InitializingBean {

	private GenericObjectPool<StatefulRedisModulesConnection<K, V>> delegate;

	private final RedisSearchConnectionFactory factory;

	@Setter
	private RedisCodec<K, V> redisCodec;

	public RediSearchTemplate(RedisSearchConnectionFactory factory) {
		this.factory = factory;
	}

	public RediSearchTemplate(RedisSearchConnectionFactory factory, RedisCodec<K, V> redisCodec) {
		this.factory = factory;
		this.redisCodec = redisCodec;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void afterPropertiesSet() throws Exception {
		if (redisCodec == null) {
			redisCodec = (RedisCodec<K, V>) RedisCodecs.jdk();
		}
		Supplier<StatefulRedisModulesConnection<K, V>> supplier = () -> factory.connect(redisCodec);
		this.delegate = ConnectionPoolSupport.createGenericObjectPool(supplier, getPoolConfig());
	}

	@SneakyThrows
	@Delegate
	protected StatefulRedisModulesConnection<K, V> borrowObject() {
		Assert.notNull(delegate, "GenericObjectPool must not be null, call afterPropertiesSet");
		return delegate.borrowObject();
	}

	protected GenericObjectPoolConfig<StatefulRedisModulesConnection<K, V>> getPoolConfig() {
		return factory.getPoolConfig();
	}

}
