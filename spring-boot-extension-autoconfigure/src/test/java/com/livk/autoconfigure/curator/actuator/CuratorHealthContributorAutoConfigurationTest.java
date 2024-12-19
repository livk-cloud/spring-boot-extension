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

package com.livk.autoconfigure.curator.actuator;

import com.livk.autoconfigure.curator.CuratorAutoConfiguration;
import com.livk.autoconfigure.curator.CuratorProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.health.HealthContributorAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * CuratorHealthContributorAutoConfigurationTest
 * </p>
 *
 * @author livk
 */
class CuratorHealthContributorAutoConfigurationTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(HealthContributorAutoConfiguration.class,
				CuratorAutoConfiguration.class, CuratorHealthContributorAutoConfiguration.class));

	@Test
	void runShouldCreateHealthIndicator() {
		this.contextRunner.withUserConfiguration(Config.class)
			.run((context) -> assertThat(context).hasSingleBean(CuratorHealthIndicator.class));
	}

	@TestConfiguration(proxyBeanMethods = false)
	@EnableConfigurationProperties(CuratorProperties.class)
	static class Config {

	}

}
