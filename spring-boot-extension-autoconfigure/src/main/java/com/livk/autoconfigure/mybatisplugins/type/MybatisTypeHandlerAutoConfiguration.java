/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.autoconfigure.mybatisplugins.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.core.mybatisplugins.type.mysql.MysqlJsonTypeHandler;
import com.livk.core.mybatisplugins.type.postgresql.PostgresJsonTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * MybatisTypeHandlerAutoConfiguration
 * </p>
 *
 * @author livk
 */
@SpringAutoService
@AutoConfiguration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
public class MybatisTypeHandlerAutoConfiguration {

	/**
	 * Mysql configuration customizer.
	 *
	 * @param mapperProvider the mapper provider
	 * @return the configuration customizer
	 */
	@Bean
	@ConditionalOnClass(com.mysql.cj.jdbc.Driver.class)
	public ConfigurationCustomizer mysqlConfigurationCustomizer(ObjectProvider<ObjectMapper> mapperProvider) {
		ObjectMapper mapper = mapperProvider.getIfUnique(JsonMapper::new);
		return configuration -> configuration.getTypeHandlerRegistry().register(new MysqlJsonTypeHandler(mapper));
	}


	/**
	 * Postgresql configuration customizer.
	 *
	 * @param mapperProvider the mapper provider
	 * @return the configuration customizer
	 */
	@Bean
	@ConditionalOnClass(org.postgresql.Driver.class)
	public ConfigurationCustomizer postgresqlConfigurationCustomizer(ObjectProvider<ObjectMapper> mapperProvider) {
		ObjectMapper mapper = mapperProvider.getIfUnique(JsonMapper::new);
		return configuration -> configuration.getTypeHandlerRegistry().register(new PostgresJsonTypeHandler(mapper));
	}
}
