package com.livk.core.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * <p>
 * CuratorConfig
 * </p>
 *
 * @author livk
 */
@Configuration
@PropertySource("classpath:env.properties")
class CuratorConfig {

	@Bean(initMethod = "start", destroyMethod = "close")
	public CuratorFramework curatorFramework(@Value("${curator.connectString}") String connectString) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(50, 10, 500);
		return CuratorFrameworkFactory.builder().retryPolicy(retryPolicy).connectString(connectString).build();
	}

	@Bean(destroyMethod = "close")
	public CuratorTemplate curatorTemplate(CuratorFramework framework) {
		return new CuratorTemplate(framework);
	}

}
