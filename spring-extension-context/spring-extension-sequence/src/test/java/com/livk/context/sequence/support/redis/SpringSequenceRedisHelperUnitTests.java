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

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author livk
 */
class SpringSequenceRedisHelperUnitTests {

	@Test
	void borrowsConnectionPerCommand() {
		RedisConnectionFactory factory = mock(RedisConnectionFactory.class);
		RedisConnection first = mock(RedisConnection.class);
		RedisConnection second = mock(RedisConnection.class);
		RedisStringCommands firstCommands = mock(RedisStringCommands.class);
		RedisStringCommands secondCommands = mock(RedisStringCommands.class);
		given(factory.getConnection()).willReturn(first, second);
		given(first.stringCommands()).willReturn(firstCommands);
		given(second.stringCommands()).willReturn(secondCommands);
		given(secondCommands.incrBy(any(), eq(10L))).willReturn(11L);

		SpringSequenceRedisHelper helper = new SpringSequenceRedisHelper(factory);
		byte[] key = "order".getBytes();
		helper.setNx(key, 1L);
		assertThat(helper.incrBy(key, 10)).isEqualTo(11L);
		helper.close();

		verify(factory, times(2)).getConnection();
		verify(first).close();
		verify(second).close();
	}

}
