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

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * Spring Data Redis backed {@link SequenceRedisHelper}.
 * <p>
 * Each command borrows a connection from the factory and returns it afterwards, so the
 * helper is safe to share across threads and does not retain a pooled connection.
 *
 * @author livk
 */
public class SpringSequenceRedisHelper implements SequenceRedisHelper {

	private final RedisConnectionFactory factory;

	/**
	 * Create a helper that borrows connections from the given factory.
	 * @param factory the redis connection factory
	 */
	public SpringSequenceRedisHelper(RedisConnectionFactory factory) {
		this.factory = factory;
	}

	@Override
	public Long incrBy(byte[] key, int step) {
		try (RedisConnection connection = this.factory.getConnection()) {
			return connection.stringCommands().incrBy(key, step);
		}
	}

	@Override
	public void setNx(byte[] key, long stepStart) {
		try (RedisConnection connection = this.factory.getConnection()) {
			connection.stringCommands().setNX(key, String.valueOf(stepStart).getBytes(StandardCharsets.UTF_8));
		}
	}

	@Override
	public void close() {
		// Connections are borrowed per command; nothing to close here.
	}

}
