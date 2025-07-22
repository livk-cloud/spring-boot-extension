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

import com.livk.context.sequence.support.DbRangeManager;
import com.livk.context.sequence.support.RangeManager;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;

/**
 * @author livk
 */
@RequiredArgsConstructor
class DbSequenceBuilder extends AbstractSequenceBuilder<DbSequenceBuilder> implements SequenceBuilder {

	/**
	 * 数据库数据源[必选]
	 */
	private final DataSource dataSource;

	@Override
	protected RangeManager createRangeManager() {
		return new DbRangeManager(dataSource);
	}

}
