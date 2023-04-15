package com.livk.autoconfigure.mybatis.inject;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.mybatis.inject.annotation.EnableSqlPlugin;
import com.livk.autoconfigure.mybatis.inject.interceptor.SqlDataInjection;
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
@SpringAutoService(EnableSqlPlugin.class)
@AutoConfiguration(before = MybatisAutoConfiguration.class)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
public class SqlDataInjectionAutoConfiguration {

    /**
     * Sql data injection configuration customizer configuration customizer.
     *
     * @return the configuration customizer
     */
    @Bean
    public ConfigurationCustomizer sqlDataInjectionConfigurationCustomizer() {
        return configuration -> configuration.addInterceptor(new SqlDataInjection());
    }
}
