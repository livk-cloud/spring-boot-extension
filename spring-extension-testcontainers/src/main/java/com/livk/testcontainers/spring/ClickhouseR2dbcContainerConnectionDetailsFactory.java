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

package com.livk.testcontainers.spring;

import com.livk.auto.service.annotation.SpringFactories;
import com.livk.testcontainers.containers.r2dbc.ClickHouseR2dbcContainer;
import com.livk.testcontainers.containers.r2dbc.ClickhouseR2DBCDatabaseContainer;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcConnectionDetails;
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;

/**
 * @author livk
 */
@SpringFactories(ConnectionDetailsFactory.class)
class ClickhouseR2dbcContainerConnectionDetailsFactory
		extends ContainerConnectionDetailsFactory<ClickHouseR2dbcContainer, R2dbcConnectionDetails> {

	ClickhouseR2dbcContainerConnectionDetailsFactory() {
		super(ANY_CONNECTION_NAME, "io.r2dbc.spi.ConnectionFactoryOptions");
	}

	@Override
	protected R2dbcConnectionDetails getContainerConnectionDetails(
			ContainerConnectionSource<ClickHouseR2dbcContainer> source) {
		return new ClickhouseR2dbcDatabaseContainerConnectionDetails(source);
	}

	private static final class ClickhouseR2dbcDatabaseContainerConnectionDetails
			extends ContainerConnectionDetails<ClickHouseR2dbcContainer> implements R2dbcConnectionDetails {

		ClickhouseR2dbcDatabaseContainerConnectionDetails(ContainerConnectionSource<ClickHouseR2dbcContainer> source) {
			super(source);
		}

		@Override
		public ConnectionFactoryOptions getConnectionFactoryOptions() {
			return ClickhouseR2DBCDatabaseContainer.getOptions(getContainer());
		}

	}

}
