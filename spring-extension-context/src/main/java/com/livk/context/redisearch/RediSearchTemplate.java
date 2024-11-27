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
import com.redis.lettucemod.api.async.RedisModulesAsyncCommands;
import com.redis.lettucemod.api.reactive.RedisModulesReactiveCommands;
import com.redis.lettucemod.api.sync.RedisModulesCommands;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisConnectionStateListener;
import io.lettuce.core.api.push.PushListener;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.protocol.RedisCommand;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author livk
 */
public class RediSearchTemplate<K, V> implements InitializingBean {

	private GenericObjectPool<StatefulRedisModulesConnection<K, V>> delegate;

	private final RediSearchConnectionFactory factory;

	@Setter
	private RedisCodec<K, V> redisCodec;

	public RediSearchTemplate(RediSearchConnectionFactory factory) {
		this.factory = factory;
	}

	public RediSearchTemplate(RediSearchConnectionFactory factory, RedisCodec<K, V> redisCodec) {
		this.factory = factory;
		this.redisCodec = redisCodec;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void afterPropertiesSet() {
		if (redisCodec == null) {
			redisCodec = (RedisCodec<K, V>) RedisCodecs.jdk();
		}
		Supplier<StatefulRedisModulesConnection<K, V>> supplier = () -> factory.connect(redisCodec);
		this.delegate = ConnectionPoolSupport.createGenericObjectPool(supplier, getPoolConfig());
	}

	@SneakyThrows
	protected StatefulRedisModulesConnection<K, V> borrowObject() {
		Assert.notNull(delegate, "GenericObjectPool must not be null, call afterPropertiesSet");
		return delegate.borrowObject();
	}

	protected GenericObjectPoolConfig<StatefulRedisModulesConnection<K, V>> getPoolConfig() {
		return factory.getPoolConfig();
	}

	public RedisModulesCommands<K, V> sync() {
		return this.borrowObject().sync();
	}

	public RedisModulesAsyncCommands<K, V> async() {
		return this.borrowObject().async();
	}

	public RedisModulesReactiveCommands<K, V> reactive() {
		return this.borrowObject().reactive();
	}

	public boolean isMulti() {
		return this.borrowObject().isMulti();
	}

	public void addListener(PushListener listener) {
		this.borrowObject().addListener(listener);
	}

	public void removeListener(PushListener listener) {
		this.borrowObject().removeListener(listener);
	}

	public void addListener(RedisConnectionStateListener listener) {
		this.borrowObject().addListener(listener);
	}

	public void removeListener(RedisConnectionStateListener listener) {
		this.borrowObject().removeListener(listener);
	}

	public void setTimeout(Duration timeout) {
		this.borrowObject().setTimeout(timeout);
	}

	public Duration getTimeout() {
		return this.borrowObject().getTimeout();
	}

	public <T> RedisCommand<K, V, T> dispatch(RedisCommand<K, V, T> command) {
		return this.borrowObject().dispatch(command);
	}

	public Collection<RedisCommand<K, V, ?>> dispatch(Collection<? extends RedisCommand<K, V, ?>> commands) {
		return this.borrowObject().dispatch(commands);
	}

	public boolean isOpen() {
		return this.borrowObject().isOpen();
	}

	public ClientOptions getOptions() {
		return this.borrowObject().getOptions();
	}

	public ClientResources getResources() {
		return this.borrowObject().getResources();
	}

	public void setAutoFlushCommands(boolean autoFlush) {
		this.borrowObject().setAutoFlushCommands(autoFlush);
	}

	public void flushCommands() {
		this.borrowObject().flushCommands();
	}

}
