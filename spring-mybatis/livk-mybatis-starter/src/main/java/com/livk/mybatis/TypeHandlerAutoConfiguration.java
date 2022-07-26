package com.livk.mybatis;

import com.baomidou.mybatisplus.autoconfigure.SqlSessionFactoryBeanCustomizer;
import com.livk.mybatis.support.postgresql.JsonTypeHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * TypeHandlerAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/7/26
 */
@AutoConfiguration
public class TypeHandlerAutoConfiguration {

    @Bean
    @ConditionalOnClass(com.mysql.cj.jdbc.Driver.class)
    public SqlSessionFactoryBeanCustomizer mysqlSqlSessionFactoryBeanCustomizer() {
        return factoryBean -> {
            factoryBean.setTypeHandlers(new com.livk.mybatis.support.mysql.JsonTypeHandler());
        };
    }

    @Bean
    @ConditionalOnClass(org.postgresql.Driver.class)
    public SqlSessionFactoryBeanCustomizer postgresqlSqlSessionFactoryBeanCustomizer() {
        return factoryBean -> {
            factoryBean.setTypeHandlers(new JsonTypeHandler());
        };
    }
}
