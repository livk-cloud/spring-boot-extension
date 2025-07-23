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

import com.livk.context.sequence.exception.SequenceException;
import com.livk.context.sequence.support.db.SqlProvider;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author livk
 */
class SequenceDbHelper {

	private static final long DELTA = 100_000_000L;

	private final JdbcClient jdbcClient;

	private final TransactionTemplate transactionTemplate;

	private final SqlProvider provider;

	private final String tableName;

	public SequenceDbHelper(DataSource dataSource, String tableName) {
		this.jdbcClient = JdbcClient.create(dataSource);
		this.transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
		this.provider = SqlProvider.fromDataSource(dataSource);
		this.tableName = tableName;
	}

	public void createTable() {
		jdbcClient.sql(provider.createTableSql(tableName)).update();
	}

	public Long selectRange(String name, long stepStart) {
		return transactionTemplate.execute(status -> {
			Optional<Long> result = jdbcClient.sql(provider.selectRangeSql(tableName))
				.param("name", name)
				.query(Long.class)
				.optional();
			if (result.isPresent()) {
				Long value = result.get();
				validateValue(value);
				return value;
			}
			else {
				insertRange(name, stepStart);
				return null;
			}
		});
	}

	public boolean updateRange(String name, long newValue, long oldValue) {
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());
		int affectedRows = jdbcClient.sql(provider.updateRangeSql(tableName))
			.param("new_val", newValue)
			.param("update_time", now)
			.param("name", name)
			.param("old_val", oldValue)
			.update();
		return affectedRows > 0;
	}

	private void insertRange(String name, long stepStart) {
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());
		jdbcClient.sql(provider.insertRangeSql(tableName))
			.param("name", name)
			.param("val", stepStart)
			.param("create_time", now)
			.param("update_time", now)
			.update();
	}

	private void validateValue(long value) {
		if (value < 0) {
			throw new SequenceException("Sequence val cannot be less than zero, val = " + value);
		}
		if (value > Long.MAX_VALUE - DELTA) {
			throw new SequenceException("Sequence val overflow, val = " + value);
		}
	}

}
