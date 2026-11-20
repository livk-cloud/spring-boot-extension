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

package com.livk.testcontainers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class DockerImageNamesTests {

	@Test
	void mysqlUsesLatestByDefault() {
		assertThat(DockerImageNames.mysql().asCanonicalNameString()).isEqualTo("mysql:latest");
		assertThat(DockerImageNames.mysql("8.0").asCanonicalNameString()).isEqualTo("mysql:8.0");
	}

	@Test
	void postgresUsesLatestByDefault() {
		assertThat(DockerImageNames.postgres().asCanonicalNameString()).isEqualTo("postgres:latest");
		assertThat(DockerImageNames.postgres("15").asCanonicalNameString()).isEqualTo("postgres:15");
	}

	@Test
	void zookeeperUsesLatestByDefault() {
		assertThat(DockerImageNames.zookeeper().asCanonicalNameString()).isEqualTo("zookeeper:latest");
		assertThat(DockerImageNames.zookeeper("3.9").asCanonicalNameString()).isEqualTo("zookeeper:3.9");
	}

	@Test
	void redisUsesLatestByDefault() {
		assertThat(DockerImageNames.redis().asCanonicalNameString()).isEqualTo("redis:latest");
		assertThat(DockerImageNames.redis("7").asCanonicalNameString()).isEqualTo("redis:7");
	}

}
