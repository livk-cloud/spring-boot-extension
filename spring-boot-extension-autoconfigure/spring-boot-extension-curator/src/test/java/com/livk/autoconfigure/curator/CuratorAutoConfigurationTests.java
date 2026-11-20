/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.autoconfigure.curator;

import com.livk.context.curator.CuratorTemplate;
import org.apache.curator.RetryPolicy;
import org.apache.curator.drivers.TracerDriver;
import org.apache.curator.ensemble.EnsembleProvider;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.RetryOneTime;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author livk
 */
class CuratorAutoConfigurationTests {

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner().withUserConfiguration(Config.class)
		.withConfiguration(AutoConfigurations.of(CuratorAutoConfiguration.class));

	@Test
	void curatorFramework() {
		this.contextRunner.run((context) -> assertThat(context).hasSingleBean(CuratorFramework.class));
	}

	@Test
	void exponentialBackoffRetry() {
		this.contextRunner.run((context) -> assertThat(context).hasSingleBean(RetryPolicy.class));
	}

	@Test
	void curatorTemplate() {
		this.contextRunner.run((context) -> assertThat(context).hasSingleBean(CuratorTemplate.class));
	}

	@Test
	void tracerDriverAndEnsembleProviderAreRegistered() {
		this.contextRunner.run((context) -> {
			assertThat(context).hasSingleBean(TracerDriver.class);
			assertThat(context).hasSingleBean(EnsembleProvider.class);
		});
	}

	@Test
	void disabledWhenPropertyFalse() {
		this.contextRunner.withPropertyValues("spring.zookeeper.curator.enabled=false")
			.run((context) -> assertThat(context).doesNotHaveBean(CuratorFramework.class)
				.doesNotHaveBean(CuratorTemplate.class));
	}

	@Test
	void backsOffWhenUserProvidesBeans() {
		this.contextRunner.withUserConfiguration(UserBeans.class).run((context) -> {
			assertThat(context).hasSingleBean(CuratorFramework.class);
			assertThat(context).hasSingleBean(RetryPolicy.class);
			assertThat(context).hasSingleBean(CuratorTemplate.class);
			assertThat(context.getBean(RetryPolicy.class)).isInstanceOf(RetryOneTime.class);
		});
	}

	@TestConfiguration(proxyBeanMethods = false)
	@EnableConfigurationProperties(CuratorProperties.class)
	static class Config {

	}

	@TestConfiguration(proxyBeanMethods = false)
	static class UserBeans {

		@Bean
		CuratorFramework curatorFramework() {
			return mock(CuratorFramework.class);
		}

		@Bean
		RetryPolicy retryPolicy() {
			return new RetryOneTime(1);
		}

		@Bean
		CuratorTemplate curatorTemplate(CuratorFramework framework) {
			return new CuratorTemplate(framework);
		}

	}

}
