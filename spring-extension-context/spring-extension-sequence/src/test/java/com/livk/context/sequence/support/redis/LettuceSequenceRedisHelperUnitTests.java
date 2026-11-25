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

package com.livk.context.sequence.support.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author livk
 */
class LettuceSequenceRedisHelperUnitTests {

	@Test
	@SuppressWarnings("unchecked")
	void delegatesCommandsAndClosesConnection() {
		RedisClient client = mock(RedisClient.class);
		StatefulRedisConnection<String, String> connection = mock(StatefulRedisConnection.class);
		RedisCommands<String, String> commands = mock(RedisCommands.class);
		given(client.connect()).willReturn(connection);
		given(connection.sync()).willReturn(commands);
		given(commands.incrby("order", 10)).willReturn(11L);

		LettuceSequenceRedisHelper helper = new LettuceSequenceRedisHelper(client);
		helper.setNx("order".getBytes(), 1L);
		assertThat(helper.incrBy("order".getBytes(), 10)).isEqualTo(11L);
		helper.close();

		verify(commands).set(eq("order"), eq("1"), any(SetArgs.class));
		verify(commands).incrby("order", 10);
		verify(connection).close();
	}

}
