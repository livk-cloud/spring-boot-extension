package com.livk.zookeeper.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * ZookeeperConfig
 * </p>
 *
 * @author livk
 * @date 2022/4/6
 */
@Configuration
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZookeeperConfig {

    @Bean
    public CuratorFramework curatorFramework(ZookeeperProperties zookeeperProperties) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(zookeeperProperties.getBaseSleepTime(),
                zookeeperProperties.getMaxRetries());
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zookeeperProperties.getServers())
                .connectionTimeoutMs(zookeeperProperties.getConnectionTimeout())
                .sessionTimeoutMs(zookeeperProperties.getSessionTimeout())
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        return client;
    }

}
