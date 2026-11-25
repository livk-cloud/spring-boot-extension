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
import com.livk.context.sequence.builder.SequenceBuilder;
import com.livk.context.sequence.exception.SequenceException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Caches one range-based {@link Sequence} per business name.
 *
 * @author livk
 */
public class RangeSequenceProvider implements SequenceProvider {

	private final RangeManager rangeManager;

	private final int step;

	private final long stepStart;

	private final ConcurrentMap<String, Sequence> sequences = new ConcurrentHashMap<>();

	/**
	 * Create a provider that shares one {@link RangeManager}.
	 * @param rangeManager the range manager
	 * @param step the range step
	 * @param stepStart the initial value, must be {@code >= 0}
	 */
	public RangeSequenceProvider(RangeManager rangeManager, int step, long stepStart) {
		Assert.isTrue(step > 0, "Sequence step must be positive");
		Assert.isTrue(stepStart >= 0, "Sequence stepStart must be greater than or equal to 0");
		this.rangeManager = rangeManager;
		this.step = step;
		this.stepStart = stepStart;
	}

	@Override
	public Sequence get(String bizName) {
		if (!StringUtils.hasText(bizName)) {
			throw new SequenceException("Sequence bizName must not be empty");
		}
		return this.sequences.computeIfAbsent(bizName, this::create);
	}

	private Sequence create(String bizName) {
		return SequenceBuilder.builder(this.rangeManager)
			.bizName(bizName)
			.step(this.step)
			.stepStart(this.stepStart)
			.build();
	}

}
