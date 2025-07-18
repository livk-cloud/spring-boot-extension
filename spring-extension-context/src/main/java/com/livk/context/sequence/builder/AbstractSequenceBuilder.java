/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.context.sequence.builder;

import com.livk.context.sequence.Sequence;
import com.livk.context.sequence.support.RangeManager;

/**
 * @author livk
 */
abstract class AbstractSequenceBuilder<T extends AbstractSequenceBuilder<T>> implements SequenceBuilder {

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

	@SuppressWarnings("unchecked")
	@Override
	public final T step(int step) {
		this.step = step;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final T bizName(String bizName) {
		this.bizName = bizName;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final T stepStart(long stepStart) {
		this.stepStart = stepStart;
		return (T) this;
	}

	@Override
	public final Sequence build() {
		RangeManager manager = createRangeManager();
		manager.step(this.step);
		manager.stepStart(stepStart);
		manager.init();
		// 构建序列号生成器
		return new DefaultRangeSequence(manager, bizName);
	}

	protected abstract RangeManager createRangeManager();

}
