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

package com.livk.context.sequence;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author livk
 */
@ToString
public class SequenceRange {

	/**
	 * 区间的序列号开始值
	 */
	@Getter
	private final long min;

	/**
	 * 区间的序列号结束值
	 */
	@Getter
	private final long max;

	/**
	 * 区间的序列号当前值
	 */
	private final AtomicLong value;

	/**
	 * 区间的序列号是否分配完毕，每次分配完毕就会去重新获取一个新的区间
	 */
	@Setter
	@Getter
	private volatile boolean over = false;

	public SequenceRange(long min, long max) {
		this.min = min;
		this.max = max;
		this.value = new AtomicLong(min);
	}

	/**
	 * 返回并递增下一个序列号
	 * @return 下一个序列号，如果返回-1表示序列号分配完毕
	 */
	public long getAndIncrement() {
		long currentValue = value.getAndIncrement();
		if (currentValue > max) {
			over = true;
			return -1;
		}

		return currentValue;
	}

}
