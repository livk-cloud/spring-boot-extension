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
import com.livk.context.sequence.SequenceRange;
import com.livk.context.sequence.exception.SequenceException;
import com.livk.context.sequence.support.RangeManager;
import org.springframework.util.Assert;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author livk
 */
class DefaultRangeSequence implements Sequence {

	/**
	 * 获取区间是加一把独占锁防止资源冲突
	 */
	private final Lock lock = new ReentrantLock();

	/**
	 * 序列号区间管理器
	 */
	private final RangeManager manager;

	/**
	 * 当前序列号区间
	 */
	private volatile SequenceRange currentRange;

	/**
	 * 需要获取区间的业务名称
	 */
	private final String name;

	public DefaultRangeSequence(RangeManager manager, String name) {
		Assert.notNull(name, "name is required");
		this.manager = manager;
		this.name = name;
	}

	@Override
	public long nextValue() {
		while (true) {
			SequenceRange range = currentRange;
			// 如果没有区间或已用尽，则加锁重新获取
			if (range == null || range.isOver()) {
				refreshRange();
				continue;
			}
			long id = range.getAndIncrement();
			if (id >= 0) {
				return id;
			}
			// 如果 getAndIncrement() 返回 <0，说明已用尽 → 循环刷新
		}
	}

	private void refreshRange() {
		lock.lock();
		try {
			if (currentRange == null || currentRange.isOver()) {
				currentRange = manager.nextRange(name);
			}
		}
		catch (Exception ex) {
			throw new SequenceException("Failed to acquire new range for name: " + name, ex);
		}
		finally {
			lock.unlock();
		}
	}

}
