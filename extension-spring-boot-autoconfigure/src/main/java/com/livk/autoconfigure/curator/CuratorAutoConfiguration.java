package com.livk.autoconfigure.curator;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.curator.support.CuratorTemplate;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * CuratorAutoConfiguration
 * </p>
 *
 * @author livk
 */
@SpringAutoService
@AutoConfiguration
@ConditionalOnClass(CuratorFramework.class)
@EnableConfigurationProperties(CuratorProperties.class)
public class CuratorAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curatorFramework(CuratorProperties curatorProperties) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(curatorProperties.getBaseSleepTime(),
                curatorProperties.getMaxRetries());
        return CuratorFrameworkFactory.builder()
                .connectString(curatorProperties.getServers())
                .connectionTimeoutMs(curatorProperties.getConnectionTimeout())
                .sessionTimeoutMs(curatorProperties.getSessionTimeout())
                .retryPolicy(retryPolicy)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public CuratorTemplate curatorTemplate(CuratorFramework curatorFramework) {
        return new CuratorTemplate(curatorFramework);
    }
}
