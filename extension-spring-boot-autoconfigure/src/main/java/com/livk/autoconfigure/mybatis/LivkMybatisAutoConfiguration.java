package com.livk.autoconfigure.mybatis;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.mybatis.annotation.EnableSqlPlugin;
import com.livk.autoconfigure.mybatis.interceptor.SqlDataInjection;
import com.livk.autoconfigure.mybatis.monitor.EnableSqlMonitor;
import com.livk.autoconfigure.mybatis.monitor.MybatisLogMonitor;
import com.livk.autoconfigure.mybatis.monitor.MybatisLogMonitorProperties;
import com.livk.autoconfigure.mybatis.monitor.SqlMonitor;
import com.livk.autoconfigure.mybatis.support.mysql.MysqlJsonTypeHandler;
import com.livk.autoconfigure.mybatis.support.postgresql.PostgresJsonTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.SqlSessionFactoryBeanCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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

    @SpringAutoService(auto = EnableSqlMonitor.class)
    @AutoConfiguration(before = MybatisAutoConfiguration.class)
    @ConditionalOnClass(SqlSessionFactory.class)
    @EnableConfigurationProperties(MybatisLogMonitorProperties.class)
    public static class MybatisLogMonitorAutoConfiguration {

        @Bean
        public ConfigurationCustomizer mybatisLogMonitorConfigurationCustomizer(MybatisLogMonitorProperties properties,
                                                                                ObjectProvider<SqlMonitor> monitorList) {
            return configuration ->
                    configuration.addInterceptor(new MybatisLogMonitor(properties, monitorList));
        }
    }

    @SpringAutoService(auto = EnableSqlPlugin.class)
    @AutoConfiguration(before = MybatisAutoConfiguration.class)
    @ConditionalOnClass(SqlSessionFactory.class)
    public class SqlDataInjectionAutoConfiguration {

        @Bean
        public ConfigurationCustomizer sqlDataInjectionConfigurationCustomizer() {
            return configuration -> configuration.addInterceptor(new SqlDataInjection());
        }
    }
}
