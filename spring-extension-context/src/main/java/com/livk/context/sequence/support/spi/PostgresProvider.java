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

package com.livk.context.sequence.support.spi;

import com.livk.auto.service.annotation.SpringFactories;
import org.springframework.boot.jdbc.DatabaseDriver;

/**
 * @author livk
 */
@SpringFactories
public class PostgresProvider implements SqlProvider {

	@Override
	public DatabaseDriver type() {
		return DatabaseDriver.POSTGRESQL;
	}

	@Override
	public String createTableSql(String tableName) {
		return String.format("CREATE TABLE IF NOT EXISTS %s (" + "id BIGSERIAL PRIMARY KEY, " + "val BIGINT NOT NULL, "
				+ "name VARCHAR(32) NOT NULL, " + "create_time TIMESTAMP NOT NULL, "
				+ "update_time TIMESTAMP NOT NULL, " + "CONSTRAINT uk_name UNIQUE (name))", tableName);
	}

	@Override
	public String insertRangeSql(String tableName) {
		return String.format(
				"INSERT INTO %s (name, val, create_time, update_time) VALUES (:name, :val, :create_time, :update_time) "
						+ "ON CONFLICT (name) DO NOTHING",
				tableName);
	}

	@Override
	public String updateRangeSql(String tableName) {
		return String.format(
				"UPDATE %s SET val = :new_val, update_time = :update_time WHERE name = :name AND val = :old_val",
				tableName);
	}

	@Override
	public String selectRangeSql(String tableName) {
		return String.format("SELECT val FROM %s WHERE name = :name", tableName);
	}

}
