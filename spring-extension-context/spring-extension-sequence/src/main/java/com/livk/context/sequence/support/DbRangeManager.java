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
import com.livk.context.sequence.exception.SequenceException;
import com.livk.context.sequence.support.db.SequenceDbHelper;
import lombok.Setter;
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
public class DbRangeManager extends AbstractRangeManager implements RangeManager {

	/**
	 * 表名前缀，为防止数据库表名冲突，默认带上这个前缀
	 */
	private static final String TABLE_NAME = "sequence_range";

	/**
	 * 获取区间失败重试次数
	 */
	@Setter
	private int retryTimes = 5;

	private static final long DELTA = 100_000_000L;

	private final JdbcClient jdbcClient;

	private final TransactionTemplate transactionTemplate;

	private final SequenceDbHelper dbHelper;

	public DbRangeManager(DataSource dataSource) {
		this.jdbcClient = JdbcClient.create(dataSource);
		this.transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
		this.dbHelper = SequenceDbHelper.fromDataSource(dataSource);
		this.createTable();
	}

	@Override
	public SequenceRange buildNextRange(String name) {
		for (int i = 0; i < retryTimes; i++) {
			Long oldValue = this.selectRange(name, stepStart);
			if (null == oldValue) {
				// 区间不存在，重试
				continue;
			}
			long newValue = oldValue + step;
			if (this.updateRange(name, newValue, oldValue)) {
				return new SequenceRange(oldValue + 1, newValue);
			}
		}
		throw new SequenceException("Retried too many times, retryTimes = " + retryTimes);
	}

	protected void createTable() {
		jdbcClient.sql(dbHelper.createTableSql(TABLE_NAME)).update();
	}

	protected Long selectRange(String name, long stepStart) {
		return transactionTemplate.execute(status -> {
			Optional<Long> result = jdbcClient.sql(dbHelper.selectRangeSql(TABLE_NAME))
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

	protected boolean updateRange(String name, long newValue, long oldValue) {
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());
		int affectedRows = jdbcClient.sql(dbHelper.updateRangeSql(TABLE_NAME))
			.param("new_val", newValue)
			.param("update_time", now)
			.param("name", name)
			.param("old_val", oldValue)
			.update();
		return affectedRows > 0;
	}

	protected void insertRange(String name, long stepStart) {
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());
		jdbcClient.sql(dbHelper.insertRangeSql(TABLE_NAME))
			.param("name", name)
			.param("val", stepStart)
			.param("create_time", now)
			.param("update_time", now)
			.update();
	}

	protected void validateValue(long value) {
		if (value < 0) {
			throw new SequenceException("Sequence val cannot be less than zero, val = " + value);
		}
		if (value > Long.MAX_VALUE - DELTA) {
			throw new SequenceException("Sequence val overflow, val = " + value);
		}
	}

}
