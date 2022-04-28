package com.livk.zookeeper.config;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.zookeeper.config.CuratorFrameworkFactoryBean;
import org.springframework.integration.zookeeper.lock.ZookeeperLockRegistry;

/**
 * <p>
 * ZooConfig
 * </p>
 *
 * @author livk
 * @date 2022/4/7
 */
@Configuration
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZooConfig {

	@Bean
	public CuratorFramework curatorFramework(ZookeeperProperties properties) {
		return new CuratorFrameworkFactoryBean(properties.getAddress()).getObject();
	}

	@Bean
	public ZookeeperLockRegistry zookeeperLockRegistry(CuratorFramework curatorFramework) {
		return new ZookeeperLockRegistry(curatorFramework, "/zookeeper-lock");
	}

}
