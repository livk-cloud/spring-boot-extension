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

package com.livk.context.dynamic;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.SQLException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class DynamicDatasourceTests {

	static final DynamicDatasource dynamicDatasource = new DynamicDatasource();

	static final HikariDataSource primary = new HikariDataSource();

	static final DriverManagerDataSource slave1 = new DriverManagerDataSource();

	static final SingleConnectionDataSource slave2 = new SingleConnectionDataSource();

	@BeforeAll
	static void init() {
		Map<Object, Object> datasourceMap = Map.of("primary", primary, "slave1", slave1, "slave2", slave2);
		dynamicDatasource.setTargetDataSources(datasourceMap);
		dynamicDatasource.setDefaultTargetDataSource(primary);
		dynamicDatasource.afterPropertiesSet();
	}

	@Test
	void test() throws SQLException {
		assertThat(dynamicDatasource.getResolvedDefaultDataSource()).isEqualTo(primary);
		assertThat(dynamicDatasource.unwrap(HikariDataSource.class)).isEqualTo(primary);
		assertThat(dynamicDatasource.isWrapperFor(HikariDataSource.class)).isTrue();

		DataSourceContextHolder.switchDataSource("slave1");
		assertThat(dynamicDatasource.unwrap(DriverManagerDataSource.class)).isEqualTo(slave1);
		assertThat(dynamicDatasource.isWrapperFor(DriverManagerDataSource.class)).isTrue();

		DataSourceContextHolder.switchDataSource("slave2");
		assertThat(dynamicDatasource.unwrap(SingleConnectionDataSource.class)).isEqualTo(slave2);
		assertThat(dynamicDatasource.isWrapperFor(SingleConnectionDataSource.class)).isTrue();

		DataSourceContextHolder.clear();
		assertThat(dynamicDatasource.unwrap(HikariDataSource.class)).isEqualTo(primary);
		assertThat(dynamicDatasource.isWrapperFor(HikariDataSource.class)).isTrue();
	}

}
