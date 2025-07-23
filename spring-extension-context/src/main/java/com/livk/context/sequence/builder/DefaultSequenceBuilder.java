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
import com.livk.context.sequence.support.RangeManager;
import lombok.RequiredArgsConstructor;

/**
 * @author livk
 */
@RequiredArgsConstructor
class DefaultSequenceBuilder implements SequenceBuilder {

	private final RangeManager manager;

	/**
	 * 业务名称[必选]
	 */
	protected String bizName;

	/**
	 * 获取range步长[可选，默认：1000]
	 */
	protected int step = 1000;

	/**
	 * 序列号分配起始值[可选：默认：0]
	 */
	protected long stepStart = 0;

	@Override
	public final SequenceBuilder step(int step) {
		this.step = step;
		return this;
	}

	@Override
	public final SequenceBuilder bizName(String bizName) {
		this.bizName = bizName;
		return this;
	}

	@Override
	public final SequenceBuilder stepStart(long stepStart) {
		this.stepStart = stepStart;
		return this;
	}

	@Override
	public final Sequence build() {
		manager.step(this.step);
		manager.stepStart(stepStart);
		manager.init();
		return new DefaultRangeSequence(manager, bizName);
	}

}
