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

import com.livk.testcontainers.DockerImageNames;
import lombok.Getter;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * @author livk
 */
@Getter
public class ClickHouseR2dbcContainer extends GenericContainer<ClickHouseR2dbcContainer> {

	public static final Integer HTTP_PORT = 8123;

	public static final Integer NATIVE_PORT = 9000;

	private String databaseName;

	private String username;

	private String password;

	public ClickHouseR2dbcContainer() {
		this(DockerImageNames.clickhouse());
	}

	public ClickHouseR2dbcContainer(String dockerImageName) {
		this(DockerImageName.parse(dockerImageName));
	}

	public ClickHouseR2dbcContainer(DockerImageName dockerImageName) {
		super(dockerImageName);
		this.databaseName = "default";
		this.username = "default";
		this.password = "";
	}

	@Override
	protected void configure() {
		this.withEnv("CLICKHOUSE_DB", this.databaseName);
		this.withEnv("CLICKHOUSE_USER", this.username);
		this.withEnv("CLICKHOUSE_PASSWORD", this.password);
	}

	public ClickHouseR2dbcContainer withUsername(String username) {
		this.username = username;
		return this;
	}

	public ClickHouseR2dbcContainer withPassword(String password) {
		this.password = password;
		return this;
	}

	public ClickHouseR2dbcContainer withDatabaseName(String databaseName) {
		this.databaseName = databaseName;
		return this;
	}

}
