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

package com.livk.autoconfigure.mybatis.monitor;

import com.livk.autoconfigure.mybatis.MybatisLogMonitorAutoConfiguration;
import com.livk.autoconfigure.mybatis.MybatisLogMonitorProperties;
import com.livk.autoconfigure.mybatis.EnableSqlMonitor;
import com.livk.context.mybatis.MybatisSqlMonitor;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class MybatisSqlMonitorAutoConfigurationTest {

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner().withUserConfiguration(Config.class)
		.withBean(DataSource.class, HikariDataSource::new)
		.withConfiguration(
				AutoConfigurations.of(MybatisAutoConfiguration.class, MybatisLogMonitorAutoConfiguration.class));

	@Test
	void mybatisLogMonitorConfigurationCustomizer() {
		this.contextRunner.run((context) -> {
			SqlSessionFactory factory = context.getBean(SqlSessionFactory.class);
			List<Interceptor> interceptors = factory.getConfiguration().getInterceptors();
			assertThat(interceptors).isNotNull().hasAtLeastOneElementOfType(MybatisSqlMonitor.class);
		});
	}

	@TestConfiguration
	@EnableSqlMonitor
	@EnableConfigurationProperties(MybatisLogMonitorProperties.class)
	static class Config {

	}

}
