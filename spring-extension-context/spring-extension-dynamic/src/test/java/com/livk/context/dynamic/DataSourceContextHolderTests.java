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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class DataSourceContextHolderTests {

	@AfterEach
	void tearDown() {
		DataSourceContextHolder.clear();
	}

	@Test
	void switchDataSourceSetsValue() {
		DataSourceContextHolder.switchDataSource("mysql");
		assertThat(DataSourceContextHolder.getDataSource()).isEqualTo("mysql");
	}

	@Test
	void switchDataSourceNonInheritableNotVisibleInChildThread() throws Exception {
		DataSourceContextHolder.switchDataSource("mysql", false);
		assertThat(DataSourceContextHolder.getDataSource()).isEqualTo("mysql");

		try (ExecutorService service = Executors.newSingleThreadExecutor()) {
			Future<String> future = service.submit(DataSourceContextHolder::getDataSource);
			assertThat(future.get()).isNull();
		}
	}

	@Test
	void switchDataSourceInheritableVisibleInChildThread() throws Exception {
		DataSourceContextHolder.switchDataSource("mysql", true);
		assertThat(DataSourceContextHolder.getDataSource()).isEqualTo("mysql");

		try (ExecutorService service = Executors.newSingleThreadExecutor()) {
			Future<String> future = service.submit(DataSourceContextHolder::getDataSource);
			assertThat(future.get()).isEqualTo("mysql");
		}
	}

	@Test
	void clearRemovesDataSource() {
		DataSourceContextHolder.switchDataSource("mysql");
		DataSourceContextHolder.clear();
		assertThat(DataSourceContextHolder.getDataSource()).isNull();
	}

	@Test
	void switchDataSourceWithEmptyStringClears() {
		DataSourceContextHolder.switchDataSource("mysql");
		DataSourceContextHolder.switchDataSource("");
		assertThat(DataSourceContextHolder.getDataSource()).isNull();
	}

	@Test
	void switchDataSourceWithNullClears() {
		DataSourceContextHolder.switchDataSource("mysql");
		DataSourceContextHolder.switchDataSource(null);
		assertThat(DataSourceContextHolder.getDataSource()).isNull();
	}

	@Test
	void getDataSourceReturnsNullWhenNotSet() {
		assertThat(DataSourceContextHolder.getDataSource()).isNull();
	}

	@Test
	void switchToInheritableClearsNonInheritable() {
		DataSourceContextHolder.switchDataSource("mysql", false);
		DataSourceContextHolder.switchDataSource("postgres", true);
		assertThat(DataSourceContextHolder.getDataSource()).isEqualTo("postgres");
	}

	@Test
	void switchToNonInheritableClearsInheritable() {
		DataSourceContextHolder.switchDataSource("mysql", true);
		DataSourceContextHolder.switchDataSource("postgres", false);
		assertThat(DataSourceContextHolder.getDataSource()).isEqualTo("postgres");
	}

}
