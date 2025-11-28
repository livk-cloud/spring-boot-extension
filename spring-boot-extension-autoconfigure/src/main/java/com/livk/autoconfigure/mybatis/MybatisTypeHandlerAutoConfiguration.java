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

package com.livk.autoconfigure.mybatis;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.context.mybatis.type.mysql.MysqlJsonTypeHandler;
import com.livk.context.mybatis.type.postgresql.PostgresJsonTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * @author livk
 */
@SpringAutoService
@AutoConfiguration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class, ConfigurationCustomizer.class })
public class MybatisTypeHandlerAutoConfiguration {

	/**
	 * The type Mysql mybatis type handler auto configuration.
	 */
	@AutoConfiguration
	@ConditionalOnClass(com.mysql.cj.jdbc.Driver.class)
	public static class MysqlMybatisTypeHandlerAutoConfiguration {

		/**
		 * Mysql configuration customizer.
		 * @param mapperProvider the mapper provider
		 * @return the configuration customizer
		 */
		@Bean
		public ConfigurationCustomizer mysqlConfigurationCustomizer(ObjectProvider<ObjectMapper> mapperProvider) {
			ObjectMapper mapper = mapperProvider.getIfUnique(JsonMapper::new);
			return configuration -> configuration.getTypeHandlerRegistry().register(new MysqlJsonTypeHandler(mapper));
		}

	}

	/**
	 * The type Postgresql mybatis type handler auto configuration.
	 */
	@AutoConfiguration
	@ConditionalOnClass(org.postgresql.Driver.class)
	public static class PostgresqlMybatisTypeHandlerAutoConfiguration {

		/**
		 * Postgresql configuration customizer.
		 * @param mapperProvider the mapper provider
		 * @return the configuration customizer
		 */
		@Bean
		public ConfigurationCustomizer postgresqlConfigurationCustomizer(ObjectProvider<ObjectMapper> mapperProvider) {
			ObjectMapper mapper = mapperProvider.getIfUnique(JsonMapper::new);
			return configuration -> configuration.getTypeHandlerRegistry()
				.register(new PostgresJsonTypeHandler(mapper));
		}

	}

}
