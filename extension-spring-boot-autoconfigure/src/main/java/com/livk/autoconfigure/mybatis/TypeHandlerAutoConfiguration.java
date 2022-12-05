package com.livk.autoconfigure.mybatis;

import com.livk.autoconfigure.mybatis.support.mysql.MysqlJsonTypeHandler;
import com.livk.autoconfigure.mybatis.support.postgresql.PostgresJsonTypeHandler;
import org.mybatis.spring.boot.autoconfigure.SqlSessionFactoryBeanCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * TypeHandlerAutoConfiguration
 * </p>
 *
 * @author livk
 *
 */
@AutoConfiguration
public class TypeHandlerAutoConfiguration {

    @Bean
    @ConditionalOnClass(com.mysql.cj.jdbc.Driver.class)
    public SqlSessionFactoryBeanCustomizer mysqlSqlSessionFactoryBeanCustomizer() {
        return factoryBean -> factoryBean.setTypeHandlers(new MysqlJsonTypeHandler());
    }

    @Bean
    @ConditionalOnClass(org.postgresql.Driver.class)
    public SqlSessionFactoryBeanCustomizer postgresqlSqlSessionFactoryBeanCustomizer() {
        return factoryBean -> factoryBean.setTypeHandlers(new PostgresJsonTypeHandler());
    }
}
