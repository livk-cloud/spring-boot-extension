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
import com.livk.context.sequence.exception.SequenceException;
import com.livk.context.sequence.support.redis.SequenceRedisHelper;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class RedisRangeManager extends AbstractRangeManager implements RangeManager {

	/**
	 * 前缀防止key重复
	 */
	private static final String KEY_PREFIX = "x_sequence_";

	/**
	 * redis客户端
	 */
	private final SequenceRedisHelper helper;

	@Override
	public SequenceRange buildNextRange(String name) {
		byte[] realKey = getRealKey(name);
		helper.setNx(realKey, stepStart);
		Long max = helper.incrBy(realKey, step);
		if (max == null) {
			throw new SequenceException("Failed to increment sequence for: " + name);
		}
		long min = max - step + 1;
		return new SequenceRange(min, max);
	}

	private byte[] getRealKey(String name) {
		return (KEY_PREFIX + name).getBytes(StandardCharsets.UTF_8);
	}

}
