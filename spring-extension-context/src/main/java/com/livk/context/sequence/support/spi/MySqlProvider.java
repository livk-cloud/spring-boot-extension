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
public class MySqlProvider implements SqlProvider {

	@Override
	public DatabaseDriver type() {
		return DatabaseDriver.MYSQL;
	}

	@Override
	public String createTableSql(String tableName) {
		return String.format("CREATE TABLE IF NOT EXISTS %s (" + "id BIGINT(20) NOT NULL AUTO_INCREMENT, "
				+ "val BIGINT(20) NOT NULL, " + "name VARCHAR(32) NOT NULL, " + "create_time DATETIME NOT NULL, "
				+ "update_time DATETIME NOT NULL, " + "PRIMARY KEY (id), UNIQUE uk_name (name))", tableName);
	}

	@Override
	public String insertRangeSql(String tableName) {
		return String.format(
				"INSERT IGNORE INTO %s (name, val, create_time, update_time) VALUES (:name, :val, :create_time, :update_time)",
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
