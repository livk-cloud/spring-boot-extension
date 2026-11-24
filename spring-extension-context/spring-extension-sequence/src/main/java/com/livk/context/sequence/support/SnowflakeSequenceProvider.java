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

import com.livk.context.sequence.Sequence;
import com.livk.context.sequence.SequenceProvider;
import com.livk.context.sequence.exception.SequenceException;
import org.springframework.util.StringUtils;

/**
 * Snowflake sequence provider.
 * <p>
 * Every business name shares the same generator. The name is still required so call sites
 * stay consistent with range-based providers.
 *
 * @author livk
 */
public class SnowflakeSequenceProvider implements SequenceProvider {

	private final Sequence sequence;

	/**
	 * Create a provider for the given worker coordinates.
	 * @param dataCenterId the data center id
	 * @param machineId the machine id
	 */
	public SnowflakeSequenceProvider(long dataCenterId, long machineId) {
		this.sequence = Sequence.snowflake(dataCenterId, machineId);
	}

	@Override
	public Sequence get(String bizName) {
		if (!StringUtils.hasText(bizName)) {
			throw new SequenceException("Sequence bizName must not be empty");
		}
		return this.sequence;
	}

}
