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
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.codec.StringCodec;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author livk
 */
class DefaultRedisSearchConnectionFactoryTests {

	@SuppressWarnings("unchecked")
	@Test
	void shouldDelegateConnectForModulesClient() {
		RedisModulesClient client = mock(RedisModulesClient.class);

		StatefulRedisModulesConnection<String, String> connection = mock(StatefulRedisModulesConnection.class);

		given(client.connect(StringCodec.UTF8)).willReturn(connection);

		DefaultRedisSearchConnectionFactory factory = new DefaultRedisSearchConnectionFactory(client);

		StatefulRedisModulesConnection<String, String> result = factory.connect(StringCodec.UTF8);

		assertThat(result).isSameAs(connection);

		verify(client).connect(StringCodec.UTF8);
	}

	@Test
	void shouldDelegateClose() {
		RedisModulesClient client = mock(RedisModulesClient.class);

		DefaultRedisSearchConnectionFactory factory = new DefaultRedisSearchConnectionFactory(client);

		factory.close();

		verify(client).close();
	}

	@Test
	void shouldThrowForUnsupportedClientType() {
		AbstractRedisClient client = mock(AbstractRedisClient.class);

		DefaultRedisSearchConnectionFactory factory = new DefaultRedisSearchConnectionFactory(client);

		assertThatIllegalStateException().isThrownBy(() -> factory.connect(StringCodec.UTF8))
			.withMessageContaining("Unsupported client type");
	}

}
