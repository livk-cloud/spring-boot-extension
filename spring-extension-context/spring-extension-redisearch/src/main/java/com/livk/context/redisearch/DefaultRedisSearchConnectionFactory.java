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

import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.cluster.RedisModulesClusterClient;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.codec.RedisCodec;

/**
 * The Default Redis Search Connection Factory.
 *
 * @author livk
 */
final class DefaultRedisSearchConnectionFactory implements RedisSearchConnectionFactory {

	private final AbstractRedisClient client;

	DefaultRedisSearchConnectionFactory(AbstractRedisClient client) {
		this.client = client;
	}

	@Override
	public <K, V> StatefulRedisModulesConnection<K, V> connect(RedisCodec<K, V> codec) {
		if (this.client instanceof RedisModulesClient modulesClient) {
			return modulesClient.connect(codec);
		}
		else if (this.client instanceof RedisModulesClusterClient clusterClient) {
			return clusterClient.connect(codec);
		}
		throw new IllegalStateException("Unsupported client type: " + this.client.getClass().getName());
	}

	@Override
	public void close() {
		this.client.close();
	}

}
