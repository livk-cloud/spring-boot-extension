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

package com.livk.core.mybatisplugins.inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p>
 * SqlDataInjectionTest
 * </p>
 *
 * @author livk
 */
@ContextConfiguration(classes = MybatisConfig.class)
@ExtendWith(SpringExtension.class)
class SqlDataInjectionTest {

	@Autowired
	UserMapper userMapper;

	@Autowired
	DataSource dataSource;

	@BeforeEach
	void init() throws SQLException {
		ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("inject-table.sql"));
	}

	@Test
	void test() {
		User user = new User();
		user.setUsername("livk");

		userMapper.insert(user);

		User copy = userMapper.getById(1);
		assertEquals(user.getUsername(), copy.getUsername());
		assertNotNull(copy.getInsertTime());
		assertNotNull(copy.getUpdateTime());
	}

}
