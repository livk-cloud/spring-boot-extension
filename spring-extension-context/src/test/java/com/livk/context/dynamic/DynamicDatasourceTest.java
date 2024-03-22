/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.dynamic;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class DynamicDatasourceTest {

	static com.livk.context.dynamic.DynamicDatasource dynamicDatasource = new DynamicDatasource();

	static HikariDataSource primary = new HikariDataSource();

	static DriverManagerDataSource slave1 = new DriverManagerDataSource();

	static SingleConnectionDataSource slave2 = new SingleConnectionDataSource();

	@BeforeAll
	public static void init() {
		Map<Object, Object> datasourceMap = Map.of("primary", primary, "slave1", slave1, "slave2", slave2);
		dynamicDatasource.setTargetDataSources(datasourceMap);
		dynamicDatasource.setDefaultTargetDataSource(primary);
		dynamicDatasource.afterPropertiesSet();
	}

	@Test
	void test() throws SQLException {
		assertEquals(primary, dynamicDatasource.getResolvedDefaultDataSource());
		assertEquals(primary, dynamicDatasource.unwrap(HikariDataSource.class));
		assertTrue(dynamicDatasource.isWrapperFor(HikariDataSource.class));

		com.livk.context.dynamic.DataSourceContextHolder.switchDataSource("slave1");
		assertEquals(slave1, dynamicDatasource.unwrap(DriverManagerDataSource.class));
		assertTrue(dynamicDatasource.isWrapperFor(DriverManagerDataSource.class));

		com.livk.context.dynamic.DataSourceContextHolder.switchDataSource("slave2");
		assertEquals(slave2, dynamicDatasource.unwrap(SingleConnectionDataSource.class));
		assertTrue(dynamicDatasource.isWrapperFor(SingleConnectionDataSource.class));

		DataSourceContextHolder.clear();
		assertEquals(primary, dynamicDatasource.unwrap(HikariDataSource.class));
		assertTrue(dynamicDatasource.isWrapperFor(HikariDataSource.class));
	}

}
