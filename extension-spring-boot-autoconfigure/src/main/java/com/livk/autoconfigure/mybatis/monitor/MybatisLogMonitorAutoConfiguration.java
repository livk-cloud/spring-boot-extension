package com.livk.autoconfigure.mybatis.monitor;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.mybatis.monitor.annotation.EnableSqlMonitor;
import com.livk.autoconfigure.mybatis.monitor.interceptor.MybatisLogMonitor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * MybatisLogMonitorAutoConfiguration
 * </p>
 *
 * @author livk
 */
@SpringAutoService(EnableSqlMonitor.class)
@AutoConfiguration(before = MybatisAutoConfiguration.class)
@EnableConfigurationProperties(MybatisLogMonitorProperties.class)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
public class MybatisLogMonitorAutoConfiguration {

    /**
     * Mybatis log monitor configuration customizer configuration customizer.
     *
     * @param monitorProperties the properties
     * @return the configuration customizer
     */
    @Bean
    public ConfigurationCustomizer mybatisLogMonitorConfigurationCustomizer(MybatisLogMonitorProperties monitorProperties) {
        MybatisLogMonitor logMonitor = new MybatisLogMonitor();
        logMonitor.setProperties(monitorProperties.properties());
        return configuration ->
                configuration.addInterceptor(logMonitor);
    }
}
