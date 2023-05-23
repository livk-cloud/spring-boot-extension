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

package com.livk.autoconfigure.mybatis.type;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.mybatis.type.mysql.MysqlJsonTypeHandler;
import com.livk.autoconfigure.mybatis.type.postgresql.PostgresJsonTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SqlSessionFactoryBeanCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * MybatisTypeHanderAutoConfiguration
 * </p>
 *
 * @author livk
 */
@SpringAutoService
@AutoConfiguration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
public class MybatisTypeHandlerAutoConfiguration {

    /**
     * Mysql sql session factory bean customizer sql session factory bean customizer.
     *
     * @return the sql session factory bean customizer
     */
    @Bean
    @ConditionalOnClass(com.mysql.cj.jdbc.Driver.class)
    public SqlSessionFactoryBeanCustomizer mysqlSqlSessionFactoryBeanCustomizer() {
        return factoryBean -> factoryBean.setTypeHandlers(new MysqlJsonTypeHandler());
    }

    /**
     * Postgresql sql session factory bean customizer sql session factory bean customizer.
     *
     * @return the sql session factory bean customizer
     */
    @Bean
    @ConditionalOnClass(org.postgresql.Driver.class)
    public SqlSessionFactoryBeanCustomizer postgresqlSqlSessionFactoryBeanCustomizer() {
        return factoryBean -> factoryBean.setTypeHandlers(new PostgresJsonTypeHandler());
    }
}
