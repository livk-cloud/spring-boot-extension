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

package com.livk.autoconfigure.mybatis.inject;

import com.livk.autoconfigure.mybatis.EnableSqlInjector;
import com.livk.autoconfigure.mybatis.SqlDataInjectionAutoConfiguration;
import com.livk.context.mybatis.SqlDataInjection;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class SqlDataInjectionAutoConfigurationTest {

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner().withUserConfiguration(Config.class)
		.withBean(DataSource.class, HikariDataSource::new)
		.withConfiguration(
				AutoConfigurations.of(MybatisAutoConfiguration.class, SqlDataInjectionAutoConfiguration.class));

	@Test
	void sqlDataInjectionConfigurationCustomizer() {
		this.contextRunner.run((context) -> {
			SqlSessionFactory factory = context.getBean(SqlSessionFactory.class);
			List<Interceptor> interceptors = factory.getConfiguration().getInterceptors();
			assertThat(interceptors).isNotNull().hasAtLeastOneElementOfType(SqlDataInjection.class);
		});
	}

	@TestConfiguration
	@EnableSqlInjector
	static class Config {

	}

}
