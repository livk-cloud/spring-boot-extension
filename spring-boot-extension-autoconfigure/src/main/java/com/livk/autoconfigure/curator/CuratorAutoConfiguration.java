/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.autoconfigure.curator;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.curator.support.CuratorTemplate;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.ObjectProvider;
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

	/**
	 * Curator framework
	 *
	 * @param curatorProperties                  the curator properties
	 * @param curatorFrameworkBuilderCustomizers the curator framework builder customizers
	 * @return the curator framework
	 */
	@ConditionalOnMissingBean
	@Bean(initMethod = "start", destroyMethod = "close")
	public CuratorFramework curatorFramework(CuratorProperties curatorProperties,
											 ObjectProvider<CuratorFrameworkBuilderCustomizer> curatorFrameworkBuilderCustomizers) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(curatorProperties.getBaseSleepTime(),
			curatorProperties.getMaxRetries());
		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
			.connectString(curatorProperties.getServers())
			.connectionTimeoutMs(curatorProperties.getConnectionTimeout())
			.sessionTimeoutMs(curatorProperties.getSessionTimeout())
			.retryPolicy(retryPolicy);
		curatorFrameworkBuilderCustomizers.orderedStream().forEach(customizer -> customizer.customize(builder));
		return builder.build();
	}

	/**
	 * Curator template curator template.
	 *
	 * @param curatorFramework the curator framework
	 * @return the curator template
	 */
	@Bean
	@ConditionalOnMissingBean
	public CuratorTemplate curatorTemplate(CuratorFramework curatorFramework) {
		return new CuratorTemplate(curatorFramework);
	}
}
