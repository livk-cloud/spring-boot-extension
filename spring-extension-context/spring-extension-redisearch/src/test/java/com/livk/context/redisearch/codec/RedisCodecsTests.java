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

import io.lettuce.core.codec.RedisCodec;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class RedisCodecsTests {

	@Test
	void jdkReturnsNonNull() {
		RedisCodec<Object, Object> codec = RedisCodecs.jdk();
		assertThat(codec).isNotNull().isInstanceOf(JdkRedisCodec.class);
	}

	@Test
	void jdkWithClassLoaderReturnsNonNull() {
		RedisCodec<Object, Object> codec = RedisCodecs.jdk(Thread.currentThread().getContextClassLoader());
		assertThat(codec).isNotNull().isInstanceOf(JdkRedisCodec.class);
	}

	@Test
	void jsonWithMapperReturnsNonNull() {
		JsonMapper mapper = JsonMapper.builder().build();
		RedisCodec<String, Object> codec = RedisCodecs.json(mapper);
		assertThat(codec).isNotNull().isInstanceOf(JacksonRedisCodec.class);
	}

	@Test
	void jsonWithMapperAndClassTypesReturnsNonNull() {
		JsonMapper mapper = JsonMapper.builder().build();
		RedisCodec<String, String> codec = RedisCodecs.json(mapper, String.class, String.class);
		assertThat(codec).isNotNull().isInstanceOf(JacksonRedisCodec.class);
	}

}
