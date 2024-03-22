/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.testcontainers.properties.TestcontainersPropertySourceAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnectionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author livk
 */
@Configuration
@Import({ ServiceConnectionAutoConfiguration.class, TestcontainersPropertySourceAutoConfiguration.class })
class CuratorConfig {

	@Bean(initMethod = "start", destroyMethod = "close")
	public CuratorFramework curatorFramework(@Value("${curator.connectString}") String connectString) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(50, 10, 500);
		return CuratorFrameworkFactory.builder().retryPolicy(retryPolicy).connectString(connectString).build();
	}

	@Bean(destroyMethod = "close")
	public com.livk.context.curator.CuratorTemplate curatorTemplate(CuratorFramework framework) {
		return new CuratorTemplate(framework);
	}

}
