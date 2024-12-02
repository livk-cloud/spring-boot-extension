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

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * <p>
 * DataSourceContextHolderTest
 * </p>
 *
 * @author livk
 */
class DataSourceContextHolderTest {

	@Test
	void test() {

		try (ExecutorService service = Executors.newFixedThreadPool(1)) {
			String datasource = "mysql";

			DataSourceContextHolder.switchDataSource(datasource, true);
			assertEquals(datasource, DataSourceContextHolder.getDataSource());
			service.submit(() -> {
				assertEquals(datasource, DataSourceContextHolder.getDataSource());
			});
			DataSourceContextHolder.clear();

			DataSourceContextHolder.switchDataSource(datasource, false);
			assertEquals(datasource, DataSourceContextHolder.getDataSource());
			service.submit(() -> {
				assertNull(DataSourceContextHolder.getDataSource());
			});
			DataSourceContextHolder.clear();
		}
	}

}
