package com.livk.autoconfigure.mybatis;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.mybatis.support.mysql.MysqlJsonTypeHandler;
import com.livk.autoconfigure.mybatis.support.postgresql.PostgresJsonTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SqlSessionFactoryBeanCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * LivkMybatisAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2023/1/4
 */
@SpringAutoService
@AutoConfiguration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
public class LivkMybatisAutoConfiguration {

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
