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

package com.livk.autoconfigure.mybatis.type;

import com.livk.context.mybatis.type.mysql.MysqlJsonTypeHandler;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import javax.sql.DataSource;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class MybatisTypeHandlerAutoConfigurationTest {

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withBean(DataSource.class, HikariDataSource::new)
		.withConfiguration(
				AutoConfigurations.of(MybatisAutoConfiguration.class, MybatisTypeHandlerAutoConfiguration.class));

	@Test
	void testMysql() {
		this.contextRunner.run((context) -> {
			SqlSessionFactory factory = context.getBean(SqlSessionFactory.class);
			Collection<TypeHandler<?>> typeHandlers = factory.getConfiguration()
				.getTypeHandlerRegistry()
				.getTypeHandlers();
			assertThat(typeHandlers).isNotNull().hasAtLeastOneElementOfType(MysqlJsonTypeHandler.class);
		});
	}

	@Test
	void testPgsql() {
		this.contextRunner.run((context) -> {
			SqlSessionFactory factory = context.getBean(SqlSessionFactory.class);
			Collection<TypeHandler<?>> typeHandlers = factory.getConfiguration()
				.getTypeHandlerRegistry()
				.getTypeHandlers();
			assertThat(typeHandlers).isNotNull().hasAtLeastOneElementOfType(MysqlJsonTypeHandler.class);
		});
	}

}
