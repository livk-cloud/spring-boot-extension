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

import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author livk
 */
public interface RediSearchConnectionFactory {

	default StatefulRedisModulesConnection<String, String> connect() {
		return this.connect(StringCodec.UTF8);
	}

	<K, V> StatefulRedisModulesConnection<K, V> connect(RedisCodec<K, V> codec);

	void close();

	default <T> GenericObjectPoolConfig<T> getPoolConfig() {
		return new GenericObjectPoolConfig<>();
	}

	static RediSearchConnectionFactory create(AbstractRedisClient client) {
		return FactoryProxySupport.newProxy(client);
	}

}
