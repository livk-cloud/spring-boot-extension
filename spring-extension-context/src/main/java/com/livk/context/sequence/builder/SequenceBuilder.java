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

package com.livk.context.sequence.builder;

import com.livk.context.sequence.Sequence;
import io.lettuce.core.RedisClient;

import javax.sql.DataSource;

/**
 * @author livk
 */
public interface SequenceBuilder {

	SequenceBuilder step(int step);

	SequenceBuilder bizName(String bizName);

	SequenceBuilder stepStart(long stepStart);

	Sequence build();

	static SequenceBuilder builder(DataSource dataSource) {
		return new DbSequenceBuilder(dataSource);
	}

	static SequenceBuilder builder(RedisClient redisClient) {
		return new RedisSequenceBuilder(redisClient);
	}

}
