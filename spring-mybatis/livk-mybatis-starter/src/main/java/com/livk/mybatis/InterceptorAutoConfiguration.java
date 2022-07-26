package com.livk.mybatis;

import com.livk.mybatis.interceptor.SqlInterceptor;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * InterceptorAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/7/26
 */
@AutoConfiguration
public class InterceptorAutoConfiguration {

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.addInterceptor(new SqlInterceptor());
    }
}
