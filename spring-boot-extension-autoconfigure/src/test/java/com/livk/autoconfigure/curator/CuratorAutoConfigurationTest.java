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

package com.livk.autoconfigure.curator;

import com.livk.context.curator.CuratorTemplate;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class CuratorAutoConfigurationTest {

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(CuratorAutoConfiguration.class));

	@Test
	void curatorFramework() {
		this.contextRunner.withUserConfiguration(Config.class).run((context) -> {
			assertEquals(1, context.getBeanProvider(CuratorFramework.class).stream().count());
		});
	}

	@Test
	void exponentialBackoffRetry() {
		this.contextRunner.withUserConfiguration(Config.class).run((context) -> {
			assertEquals(1, context.getBeanProvider(RetryPolicy.class).stream().count());
		});
	}

	@Test
	void curatorTemplate() {
		this.contextRunner.withUserConfiguration(Config.class).run((context) -> {
			assertEquals(1, context.getBeanProvider(CuratorTemplate.class).stream().count());
		});
	}

	@Configuration(proxyBeanMethods = false)
	@EnableConfigurationProperties(CuratorProperties.class)
	static class Config {

	}

}
