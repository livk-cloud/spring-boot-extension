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

package com.livk.autoconfigure.mybatis;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.context.mybatis.SqlDataInjection;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * SqlDataInjectionAutoConfiguration
 * </p>
 *
 * @author livk
 */
@SpringAutoService(EnableSqlInjector.class)
@AutoConfiguration(before = MybatisAutoConfiguration.class)
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
public class SqlDataInjectionAutoConfiguration {

	/**
	 * Sql data injection configuration customizer configuration customizer.
	 * @return the configuration customizer
	 */
	@Bean
	public ConfigurationCustomizer sqlDataInjectionConfigurationCustomizer() {
		return configuration -> configuration.addInterceptor(new SqlDataInjection());
	}

}
