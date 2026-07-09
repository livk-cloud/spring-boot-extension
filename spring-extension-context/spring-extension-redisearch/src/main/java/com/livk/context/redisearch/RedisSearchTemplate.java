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

package com.livk.context.redisearch;

import com.livk.context.redisearch.codec.RedisSearchCodecs;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.async.RedisModulesAsyncCommands;
import com.redis.lettucemod.api.reactive.RedisModulesReactiveCommands;
import com.redis.lettucemod.api.sync.RedisModulesCommands;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author livk
 */
public class RedisSearchTemplate<K, V> implements InitializingBean {

	private GenericObjectPool<StatefulRedisModulesConnection<K, V>> delegate;

	private final RedisSearchConnectionFactory factory;

	@Setter
	private RedisCodec<K, V> redisCodec;

	@Setter
	private GenericObjectPoolConfig<StatefulRedisModulesConnection<K, V>> poolConfig;

	public RedisSearchTemplate(RedisSearchConnectionFactory factory) {
		this.factory = factory;
	}

	public RedisSearchTemplate(RedisSearchConnectionFactory factory, RedisCodec<K, V> redisCodec) {
		this.factory = factory;
		this.redisCodec = redisCodec;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void afterPropertiesSet() {
		if (redisCodec == null) {
			redisCodec = (RedisCodec<K, V>) RedisSearchCodecs.jdk();
		}
		Supplier<StatefulRedisModulesConnection<K, V>> supplier = () -> factory.connect(redisCodec);
		this.delegate = ConnectionPoolSupport.createGenericObjectPool(supplier, getPoolConfig());
	}

	@SneakyThrows
	private StatefulRedisModulesConnection<K, V> borrowObject() {
		Assert.notNull(delegate, "GenericObjectPool must not be null, call afterPropertiesSet");
		return delegate.borrowObject();
	}

	private GenericObjectPoolConfig<StatefulRedisModulesConnection<K, V>> getPoolConfig() {
		if (poolConfig != null) {
			return poolConfig;
		}
		return new GenericObjectPoolConfig<>();
	}

	/**
	 * Execute a callback with a sync commands instance. The connection is safely borrowed
	 * from the pool and returned after the callback completes.
	 * @param callback the callback to execute
	 * @param <T> the return type
	 * @return the result of the callback
	 */
	public <T> T executeSync(Function<RedisModulesCommands<K, V>, T> callback) {
		return submit(connection -> callback.apply(connection.sync()));
	}

	/**
	 * Execute a callback with an async commands instance. The connection is safely
	 * borrowed from the pool and returned after the callback completes.
	 * @param callback the callback to execute
	 * @param <T> the return type
	 * @return the result of the callback
	 */
	public <T> T executeAsync(Function<RedisModulesAsyncCommands<K, V>, T> callback) {
		return submit(connection -> callback.apply(connection.async()));
	}

	/**
	 * Execute a callback with a reactive commands instance. The connection is safely
	 * borrowed from the pool and returned after the callback completes.
	 * @param callback the callback to execute
	 * @param <T> the return type
	 * @return the result of the callback
	 */
	public <T> T executeReactive(Function<RedisModulesReactiveCommands<K, V>, T> callback) {
		return submit(connection -> callback.apply(connection.reactive()));
	}

	/**
	 * Execute a callback with a connection. The connection is safely borrowed from the
	 * pool and returned after the callback completes.
	 * @param callback the callback to execute
	 * @param <T> the return type
	 * @return the result of the callback
	 */
	public <T> T execute(Function<StatefulRedisModulesConnection<K, V>, T> callback) {
		return submit(callback);
	}

	/**
	 * Execute a callback with a connection without returning a result. The connection is
	 * safely borrowed from the pool and returned after the callback completes.
	 * @param callback the callback to execute
	 */
	public void executeVoid(Consumer<StatefulRedisModulesConnection<K, V>> callback) {
		doExecute(callback);
	}

	private <T> T submit(Function<StatefulRedisModulesConnection<K, V>, T> command) {
		try (StatefulRedisModulesConnection<K, V> borrowed = this.borrowObject()) {
			return command.apply(borrowed);
		}
	}

	private void doExecute(Consumer<StatefulRedisModulesConnection<K, V>> command) {
		try (StatefulRedisModulesConnection<K, V> borrowed = this.borrowObject()) {
			command.accept(borrowed);
		}
	}

}
