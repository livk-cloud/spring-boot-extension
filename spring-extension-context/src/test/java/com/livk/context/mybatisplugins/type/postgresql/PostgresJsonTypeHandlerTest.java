/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.mybatisplugins.type.postgresql;

import com.livk.commons.jackson.util.JsonMapperUtils;
import com.livk.testcontainers.containers.PostgresqlContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * PostgresJsonTypeHandlerTest
 * </p>
 *
 * @author livk
 */
@SpringJUnitConfig(MybatisConfig.class)
@Testcontainers(disabledWithoutDocker = true)
class PostgresJsonTypeHandlerTest {

	@Container
	@ServiceConnection
	static PostgresqlContainer postgresql = new PostgresqlContainer().withEnv("POSTGRES_PASSWORD", "123456")
		.withDatabaseName("mybatis");

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.username", postgresql::getUsername);
		registry.add("spring.datasource.password", postgresql::getPassword);
		registry.add("spring.datasource.url", () -> "jdbc:postgresql://" + postgresql.getHost() + ":"
				+ postgresql.getMappedPort(5432) + "/" + postgresql.getDatabaseName());
	}

	@Autowired
	DataSource dataSource;

	@Autowired
	UserMapper userMapper;

	@BeforeEach
	void init() throws SQLException {
		ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("type-pgsql-table.sql"));
	}

	@Test
	void test() {
		String json = """
				{
				  "mark": "livk"
				}""";
		User user = new User();
		user.setUsername("admin");
		user.setPassword("admin");
		user.setDes(JsonMapperUtils.readTree(json));

		userMapper.insert(user);
		User first = userMapper.selectList().getFirst();
		assertEquals(user.getUsername(), first.getUsername());
		assertEquals(user.getPassword(), first.getPassword());
		assertEquals(user.getDes(), first.getDes());
		assertEquals("livk", first.getDes().get("mark").asText());
	}

}
