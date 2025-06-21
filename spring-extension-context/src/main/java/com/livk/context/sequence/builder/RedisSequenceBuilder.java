/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.sequence.builder;

import com.livk.context.sequence.support.RangeManager;
import com.livk.context.sequence.support.RedisRangeManager;
import io.lettuce.core.RedisClient;
import lombok.RequiredArgsConstructor;

/**
 * @author livk
 */
@RequiredArgsConstructor
class RedisSequenceBuilder extends AbstractSequenceBuilder<RedisSequenceBuilder> implements SequenceBuilder {

	private final RedisClient redisClient;

	@Override
	protected RangeManager createRangeManager() {
		return new RedisRangeManager(redisClient);
	}

}
