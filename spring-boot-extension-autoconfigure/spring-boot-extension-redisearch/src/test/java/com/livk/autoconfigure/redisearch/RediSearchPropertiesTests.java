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

package com.livk.autoconfigure.redisearch;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class RediSearchPropertiesTests {

	final RediSearchProperties properties = new RediSearchProperties();

	@Test
	void defaultHost() {
		assertThat(properties.getHost()).isEqualTo("localhost");
	}

	@Test
	void defaultPort() {
		assertThat(properties.getPort()).isEqualTo(6379);
	}

	@Test
	void defaultDatabase() {
		assertThat(properties.getDatabase()).isZero();
	}

	@Test
	void defaultSsl() {
		assertThat(properties.getSsl()).isFalse();
	}

	@Test
	void defaultPoolSettings() {
		RediSearchProperties.Pool pool = properties.getPool();
		assertThat(pool.getEnabled()).isTrue();
		assertThat(pool.getMaxIdle()).isEqualTo(8);
		assertThat(pool.getMinIdle()).isZero();
		assertThat(pool.getMaxActive()).isEqualTo(8);
		assertThat(pool.getMaxWait()).isEqualTo(Duration.ofMillis(-1));
	}

	@Test
	void defaultUsernameAndPasswordAreNull() {
		assertThat(properties.getUsername()).isNull();
		assertThat(properties.getPassword()).isNull();
	}

	@Test
	void defaultClusterIsNull() {
		assertThat(properties.getCluster()).isNull();
	}

}
