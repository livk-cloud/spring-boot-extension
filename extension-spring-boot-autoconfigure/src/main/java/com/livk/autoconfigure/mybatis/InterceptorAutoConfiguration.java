package com.livk.autoconfigure.mybatis;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.mybatis.interceptor.SqlInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * InterceptorAutoConfiguration
 * </p>
 *
 * @author livk
 *
 */
@SpringAutoService
@AutoConfiguration(before = MybatisAutoConfiguration.class)
@ConditionalOnClass(SqlSessionFactory.class)
@ImportAutoConfiguration(TypeHandlerAutoConfiguration.class)
public class InterceptorAutoConfiguration {

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.addInterceptor(new SqlInterceptor());
    }
}
