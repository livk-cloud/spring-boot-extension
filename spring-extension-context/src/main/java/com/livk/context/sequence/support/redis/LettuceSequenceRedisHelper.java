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
import io.lettuce.core.api.sync.RedisCommands;
import lombok.RequiredArgsConstructor;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class LettuceSequenceRedisHelper implements SequenceRedisHelper {

	private final RedisClient redisClient;

	private RedisCommands<String, String> commands;

	@Override
	public Long incrBy(byte[] key, int step) {
		return commands.incrby(new String(key), step);
	}

	@Override
	public void setNx(byte[] key, long stepStart) {
		commands.setnx(new String(key), String.valueOf(stepStart));
	}

	@Override
	public void init() {
		commands = redisClient.connect().sync();
	}

}
