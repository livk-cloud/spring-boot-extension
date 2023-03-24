package com.livk.autoconfigure.mybatis;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.mybatis.monitor.EnableSqlMonitor;
import com.livk.autoconfigure.mybatis.monitor.MybatisLogMonitor;
import com.livk.autoconfigure.mybatis.monitor.MybatisLogMonitorProperties;
import com.livk.autoconfigure.mybatis.monitor.SqlMonitor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
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
     * @param properties  the properties
     * @param monitorList the monitor list
     * @return the configuration customizer
     */
    @Bean
    public ConfigurationCustomizer mybatisLogMonitorConfigurationCustomizer(MybatisLogMonitorProperties properties,
                                                                            ObjectProvider<SqlMonitor> monitorList) {
        return configuration ->
                configuration.addInterceptor(new MybatisLogMonitor(properties, monitorList));
    }
}
