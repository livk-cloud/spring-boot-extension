/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.context.sequence;

import com.livk.context.sequence.builder.SequenceBuilder;
import com.livk.testcontainers.DockerImageNames;
import com.livk.testcontainers.containers.PostgresqlContainer;
import com.redis.testcontainers.RedisContainer;
import com.zaxxer.hikari.HikariDataSource;
import io.lettuce.core.RedisClient;
import org.junit.jupiter.api.Test;
import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.properties.TestcontainersPropertySourceAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.testcontainers.service.connection.ServiceConnectionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
@SpringJUnitConfig(SequenceTest.Config.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class SequenceTest {

	@Container
	@ServiceConnection
	static final PostgresqlContainer postgresql = new PostgresqlContainer().withEnv("POSTGRES_PASSWORD", "123456")
		.withDatabaseName("sequence");

	@Container
	@ServiceConnection
	static final RedisContainer redis = new RedisContainer(DockerImageNames.redis());

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.username", postgresql::getUsername);
		registry.add("spring.datasource.password", postgresql::getPassword);
		registry.add("spring.datasource.url", () -> "jdbc:postgresql://" + postgresql.getHost() + ":"
				+ postgresql.getFirstMappedPort() + "/" + postgresql.getDatabaseName());

		registry.add("spring.data.redis.host", redis::getHost);
		registry.add("spring.data.redis.port", redis::getFirstMappedPort);
	}

	@Autowired
	DataSource dataSource;

	@Autowired
	RedisClient redisClient;

	@Test
	void testDb() {
		Sequence sequence = SequenceBuilder.builder(dataSource).bizName("test-sequence").build();
		assertNotNull(sequence);
		for (int i = 0; i < 10; i++) {
			assertEquals(i + 1, sequence.nextValue());
		}
	}

	@Test
	void testRedis() {
		Sequence sequence = SequenceBuilder.builder(redisClient).bizName("test-sequence").build();
		assertNotNull(sequence);
		for (int i = 0; i < 10; i++) {
			assertEquals(i + 1, sequence.nextValue());
		}
	}

	@Test
	void test() {
		Sequence sequence = Sequence.snowflake(1, 2);
		assertNotNull(sequence);
		Set<Long> generatedIds = new HashSet<>();
		for (int i = 0; i < 1000; i++) { // 增加迭代次数以更严格测试
			Long nextId = sequence.nextValue();
			assertNotNull(nextId);
			assertTrue(generatedIds.add(nextId), "ID should be unique");
		}
		assertEquals(1000, generatedIds.size(), "All generated IDs should be unique");
	}

	@TestConfiguration
	@Import({ ServiceConnectionAutoConfiguration.class, TestcontainersPropertySourceAutoConfiguration.class })
	static class Config {

		@Bean(destroyMethod = "close")
		public HikariDataSource dataSource(@Value("${spring.datasource.url}") String url,
				@Value("${spring.datasource.username}") String username,
				@Value("${spring.datasource.password}") String password) {
			HikariDataSource dataSource = new HikariDataSource();
			dataSource.setDriverClassName(Driver.class.getName());
			dataSource.setJdbcUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			return dataSource;
		}

		@Bean(destroyMethod = "close")
		public RedisClient redisClient(@Value("${spring.data.redis.host}") String host,
				@Value("${spring.data.redis.port}") int port) {
			return RedisClient.create("redis://" + host + ":" + port);
		}

	}

}
