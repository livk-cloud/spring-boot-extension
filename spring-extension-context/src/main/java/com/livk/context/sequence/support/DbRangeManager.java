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

import com.livk.commons.util.ObjectUtils;
import com.livk.context.sequence.SequenceRange;
import com.livk.context.sequence.exception.SequenceException;

import javax.sql.DataSource;

/**
 * @author livk
 */
public class DbRangeManager extends AbstractRangeManager implements RangeManager {

	/**
	 * 表名前缀，为防止数据库表名冲突，默认带上这个前缀
	 */
	private final static String TABLE_NAME = "sequence_range";

	/**
	 * 获取区间失败重试次数
	 */
	private final static int retryTimes = 5;

	private final SequenceDbHelper helper;

	public DbRangeManager(DataSource dataSource) {
		this.helper = new SequenceDbHelper(dataSource, TABLE_NAME);
	}

	@Override
	public SequenceRange nextRange(String name) {
		if (ObjectUtils.isEmpty(name)) {
			throw new SequenceException("[DbSeqRangeMgr-nextRange] name is empty.");
		}

		Long oldValue;
		long newValue;

		for (int i = 0; i < retryTimes; i++) {
			oldValue = helper.selectRange(name, stepStart);

			if (null == oldValue) {
				// 区间不存在，重试
				continue;
			}

			newValue = oldValue + step;

			if (helper.updateRange(name, newValue, oldValue)) {
				return new SequenceRange(oldValue + 1, newValue);
			}
			// else 失败重试
		}

		throw new SequenceException("Retried too many times, retryTimes = " + retryTimes);
	}

}
