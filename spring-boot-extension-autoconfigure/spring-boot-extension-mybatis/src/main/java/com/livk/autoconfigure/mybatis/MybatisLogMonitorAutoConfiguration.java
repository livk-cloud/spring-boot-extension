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

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.context.mybatis.MybatisSqlMonitor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author livk
 */
@SpringAutoService(EnableSqlMonitor.class)
@AutoConfiguration(before = MybatisAutoConfiguration.class)
@EnableConfigurationProperties(MybatisLogMonitorProperties.class)
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
public class MybatisLogMonitorAutoConfiguration {

	/**
	 * Mybatis log monitor configuration customizer configuration customizer.
	 * @param monitorProperties the properties
	 * @return the configuration customizer
	 */
	@Bean
	public ConfigurationCustomizer mybatisLogMonitorConfigurationCustomizer(
			MybatisLogMonitorProperties monitorProperties, ApplicationContext applicationContext) {
		MybatisSqlMonitor logMonitor = new MybatisSqlMonitor(applicationContext);
		logMonitor.setProperties(monitorProperties.properties());
		return configuration -> configuration.addInterceptor(logMonitor);
	}

}
