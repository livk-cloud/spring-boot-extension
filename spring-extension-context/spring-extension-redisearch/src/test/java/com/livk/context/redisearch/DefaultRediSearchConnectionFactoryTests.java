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
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author livk
 */
class DefaultRediSearchConnectionFactoryTests {

	@SuppressWarnings("unchecked")
	@Test
	void shouldDelegateConnect() {
		RedisClientStub client = mock(RedisClientStub.class);

		RedisCodec<String, String> codec = mock(RedisCodec.class);

		StatefulRedisModulesConnection<String, String> connection = mock(StatefulRedisModulesConnection.class);

		given(client.connect(codec)).willReturn(connection);

		DefaultRediSearchConnectionFactory factory = new DefaultRediSearchConnectionFactory(client);

		StatefulRedisModulesConnection<String, String> result = factory.connect(codec);

		assertThat(result).isSameAs(connection);

		verify(client).connect(codec);
	}

	@Test
	void shouldDelegateClose() {
		RedisClientStub client = mock(RedisClientStub.class);

		DefaultRediSearchConnectionFactory factory = new DefaultRediSearchConnectionFactory(client);

		factory.close();

		verify(client).close();
	}

	/**
	 * 测试专用 Stub。
	 */
	abstract static class RedisClientStub extends AbstractRedisClient {

		protected RedisClientStub() {
			super(null);
		}

		public abstract <K, V> StatefulRedisModulesConnection<K, V> connect(RedisCodec<K, V> codec);

	}

}
