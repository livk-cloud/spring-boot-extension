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

package com.livk.testcontainers.containers.r2dbc;

import com.clickhouse.r2dbc.connection.ClickHouseConnectionFactoryProvider;
import com.google.auto.service.AutoService;
import com.livk.testcontainers.DockerImageNames;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.testcontainers.r2dbc.R2DBCDatabaseContainer;
import org.testcontainers.r2dbc.R2DBCDatabaseContainerProvider;

/**
 * @author livk
 */
@AutoService(R2DBCDatabaseContainerProvider.class)
public class ClickhouseR2DBCDatabaseContainerProvider implements R2DBCDatabaseContainerProvider {

	static final String DRIVER = ClickHouseConnectionFactoryProvider.CLICKHOUSE_DRIVER;

	@Override
	public boolean supports(ConnectionFactoryOptions options) {
		return DRIVER.equals(options.getRequiredValue(ConnectionFactoryOptions.DRIVER));
	}

	@Override
	public R2DBCDatabaseContainer createContainer(ConnectionFactoryOptions options) {
		String image = DockerImageNames.CLICKHOUSE_IMAGE + ":" + options.getRequiredValue(IMAGE_TAG_OPTION);
		ClickHouseR2dbcContainer container = new ClickHouseR2dbcContainer(image)
			.withDatabaseName((String) options.getRequiredValue(ConnectionFactoryOptions.DATABASE))
			.withUsername((String) options.getRequiredValue(ConnectionFactoryOptions.USER))
			.withPassword((String) options.getRequiredValue(ConnectionFactoryOptions.PASSWORD));

		if (Boolean.TRUE.equals(options.getValue(REUSABLE_OPTION))) {
			container.withReuse(true);
		}
		return new ClickhouseR2DBCDatabaseContainer(container);
	}

}
