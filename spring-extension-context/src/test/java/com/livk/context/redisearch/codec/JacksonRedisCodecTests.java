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

package com.livk.context.redisearch.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.codec.RedisCodec;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class JacksonRedisCodecTests {

	static final ObjectMapper mapper = new ObjectMapper();

	static final RedisCodec<String, Object> codec = new JacksonRedisCodec<>(mapper, String.class, Object.class);

	@Test
	void testKey() throws Exception {
		String key = "redisKey";
		byte[] bytes = mapper.writeValueAsBytes(key);

		assertThat(codec.encodeKey(key)).isEqualTo(ByteBuffer.wrap(bytes));
		assertThat(codec.decodeKey(ByteBuffer.wrap(bytes))).isEqualTo(key);
	}

	@Test
	void testValue() throws Exception {
		Map<String, String> map = Map.of("username", "password");
		byte[] bytes = mapper.writeValueAsBytes(map);

		assertThat(codec.encodeValue(map)).isEqualTo(ByteBuffer.wrap(bytes));
		assertThat(codec.decodeValue(ByteBuffer.wrap(bytes))).isEqualTo(map);
	}

}
