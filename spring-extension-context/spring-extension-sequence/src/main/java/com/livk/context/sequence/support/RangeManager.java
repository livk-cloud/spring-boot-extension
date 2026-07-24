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

/**
 * 序列号区间管理器。
 * <p>
 * 实现应当是无状态的，区间步长与起始值由调用方在获取区间时传入，从而支持同一个管理器被多个不同配置的序列安全共享。
 *
 * @author livk
 */
public interface RangeManager {

	/**
	 * 获取指定业务名称的下一个序列号区间。
	 * @param name 业务名称
	 * @param step 区间步长
	 * @param stepStart 区间起始位置
	 * @return 序列号区间
	 */
	SequenceRange nextRange(String name, int step, long stepStart);

}
