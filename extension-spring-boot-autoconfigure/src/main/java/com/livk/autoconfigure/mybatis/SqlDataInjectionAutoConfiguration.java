package com.livk.autoconfigure.mybatis;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.mybatis.annotation.EnableSqlPlugin;
import com.livk.autoconfigure.mybatis.interceptor.SqlDataInjection;
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
 * @date 2023/1/4
 */
@SpringAutoService(auto = EnableSqlPlugin.class)
@AutoConfiguration(before = MybatisAutoConfiguration.class)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
public class SqlDataInjectionAutoConfiguration {

    @Bean
    public ConfigurationCustomizer sqlDataInjectionConfigurationCustomizer() {
        return configuration -> configuration.addInterceptor(new SqlDataInjection());
    }
}
