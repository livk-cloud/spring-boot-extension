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

package com.livk.context.sequence.support.db;

import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.core.io.support.SpringFactoriesLoader;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;

/**
 * @author livk
 */
public interface SequenceDbHelper {

	DatabaseDriver type();

	String createTableSql(String tableName);

	String insertRangeSql(String tableName);

	String updateRangeSql(String tableName);

	String selectRangeSql(String tableName);

	static SequenceDbHelper fromDataSource(DataSource dataSource) {
		String productName;
		try (Connection conn = dataSource.getConnection()) {
			DatabaseMetaData metaData = conn.getMetaData();
			productName = metaData.getDatabaseProductName();
		}
		catch (Exception ex) {
			throw new IllegalStateException("Failed to determine database type", ex);
		}
		DatabaseDriver driver = DatabaseDriver.fromProductName(productName);
		List<SequenceDbHelper> factories = SpringFactoriesLoader.loadFactories(SequenceDbHelper.class, null);
		for (SequenceDbHelper provider : factories) {
			if (driver == provider.type()) {
				return provider;
			}
		}
		throw new IllegalStateException("No SqlProvider found for database type");
	}

}
