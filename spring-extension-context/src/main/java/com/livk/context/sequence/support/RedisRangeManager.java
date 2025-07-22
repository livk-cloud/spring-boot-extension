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

package com.livk.context.sequence.support;

import com.livk.context.sequence.SequenceRange;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class RedisRangeManager extends AbstractRangeManager implements RangeManager {

	/**
	 * 前缀防止key重复
	 */
	private final static String KEY_PREFIX = "x_sequence_";

	/**
	 * redis客户端
	 */
	private final RedisClient redisClient;

	private RedisCommands<String, String> commands;

	/**
	 * 标记业务key是否存在，如果false，在取nextRange时，会取check一把 这个boolean只为提高性能，不用每次都取redis check
	 */
	private final AtomicBoolean keyAlreadyExist = new AtomicBoolean(false);

	@Override
	public SequenceRange nextRange(String name) {
		if (keyAlreadyExist.compareAndSet(false, true)) {
			String realKey = getRealKey(name);
			commands.setnx(realKey, String.valueOf(stepStart));
		}

		Long max = commands.incrby(getRealKey(name), step);
		Assert.notNull(max, "redis nextRange error");
		long min = max - step + 1;
		return new SequenceRange(min, max);
	}

	@Override
	public void init() {
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		commands = connection.sync();
	}

	private String getRealKey(String name) {
		return KEY_PREFIX + name;
	}

}
