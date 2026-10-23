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
import com.redis.lettucemod.api.reactive.RedisModulesReactiveCommands;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.Setter;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Reactive Redis Search Template.
 * <p>
 * Connection acquisition is deferred until subscription; each subscription leases its own
 * pooled connection and releases it when the publisher terminates.
 *
 * @author livk
 */
public class ReactiveRedisSearchTemplate<K, V> implements InitializingBean {

	private GenericObjectPool<StatefulRedisModulesConnection<K, V>> delegate;

	private final RedisSearchConnectionFactory factory;

	@Setter
	private RedisCodec<K, V> redisCodec;

	@Setter
	private GenericObjectPoolConfig<StatefulRedisModulesConnection<K, V>> poolConfig;

	public ReactiveRedisSearchTemplate(RedisSearchConnectionFactory factory) {
		this.factory = factory;
	}

	public ReactiveRedisSearchTemplate(RedisSearchConnectionFactory factory, RedisCodec<K, V> redisCodec) {
		this.factory = factory;
		this.redisCodec = redisCodec;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void afterPropertiesSet() {
		if (this.redisCodec == null) {
			this.redisCodec = (RedisCodec<K, V>) RedisSearchCodecs.jdk();
		}
		Supplier<StatefulRedisModulesConnection<K, V>> supplier = () -> this.factory.connect(this.redisCodec);
		this.delegate = ConnectionPoolSupport.createGenericObjectPool(supplier, getPoolConfig());
	}

	/**
	 * Execute a callback that returns a {@link Mono}. The pooled connection is leased on
	 * subscribe and released when the mono terminates.
	 * @param callback the callback to execute
	 * @param <T> the element type
	 * @return a cold mono backed by a pooled connection
	 */
	public <T> Mono<T> execute(Function<RedisModulesReactiveCommands<K, V>, Mono<T>> callback) {
		return Mono.usingWhen(Mono.fromCallable(this::borrowConnection),
				(connection) -> callback.apply(connection.reactive()),
				(connection) -> Mono.fromRunnable(() -> release(connection)));
	}

	/**
	 * Execute a callback that returns a {@link Flux}. The pooled connection is leased on
	 * subscribe and released when the flux terminates.
	 * @param callback the callback to execute
	 * @param <T> the element type
	 * @return a cold flux backed by a pooled connection
	 */
	public <T> Flux<T> executeMany(Function<RedisModulesReactiveCommands<K, V>, Flux<T>> callback) {
		return Flux.usingWhen(Mono.fromCallable(this::borrowConnection),
				(connection) -> callback.apply(connection.reactive()),
				(connection) -> Mono.fromRunnable(() -> release(connection)));
	}

	private StatefulRedisModulesConnection<K, V> borrowConnection() {
		try {
			Assert.notNull(this.delegate, "GenericObjectPool must not be null, call afterPropertiesSet");
			return this.delegate.borrowObject();
		}
		catch (Exception ex) {
			throw new RedisConnectionException("Error borrowing Redis connection", ex);
		}
	}

	private void release(StatefulRedisModulesConnection<K, V> connection) {
		if (connection != null) {
			connection.close();
		}
	}

	private GenericObjectPoolConfig<StatefulRedisModulesConnection<K, V>> getPoolConfig() {
		if (this.poolConfig != null) {
			return this.poolConfig;
		}
		return new GenericObjectPoolConfig<>();
	}

}
