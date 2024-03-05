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

package com.livk.testcontainers.r2dbc;

import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.testcontainers.lifecycle.Startable;
import org.testcontainers.r2dbc.R2DBCDatabaseContainer;

/**
 * @author livk
 */
@RequiredArgsConstructor
public final class ClickhouseR2DBCDatabaseContainer implements R2DBCDatabaseContainer {

	@Delegate(types = Startable.class)
	private final ClickHouseR2dbcContainer container;

	public static ConnectionFactoryOptions getOptions(ClickHouseR2dbcContainer container) {
		ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
			.option(ConnectionFactoryOptions.DRIVER, ClickhouseR2DBCDatabaseContainerProvider.DRIVER)
			.build();

		return new ClickhouseR2DBCDatabaseContainer(container).configure(options);
	}

	@Override
	public ConnectionFactoryOptions configure(ConnectionFactoryOptions options) {
		return options.mutate()
			.option(ConnectionFactoryOptions.HOST, container.getHost())
			.option(ConnectionFactoryOptions.PORT, container.getMappedPort(8123))
			.option(ConnectionFactoryOptions.DATABASE, container.getDatabaseName())
			.option(ConnectionFactoryOptions.USER, container.getUsername())
			.option(ConnectionFactoryOptions.PASSWORD, container.getPassword())
			.build();
	}

}
