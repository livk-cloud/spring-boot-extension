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

package com.livk.autoconfigure.redisearch.actuator;

import com.livk.autoconfigure.redisearch.RediSearchAutoConfiguration;
import com.livk.autoconfigure.redisearch.RediSearchProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.health.HealthContributorAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class RediSearchHealthContributorAutoConfigurationTests {

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withUserConfiguration(RediSearchHealthContributorAutoConfigurationTests.Config.class)
		.withConfiguration(
				AutoConfigurations.of(HealthContributorAutoConfiguration.class, JacksonAutoConfiguration.class,
						RediSearchAutoConfiguration.class, RediSearchHealthContributorAutoConfiguration.class));

	@Test
	void runShouldCreateHealthIndicator() {
		this.contextRunner.run((context) -> assertThat(context).hasSingleBean(RediSearchHealthIndicator.class));
	}

	@Test
	void runWhenDisabledShouldNotCreateIndicator() {
		this.contextRunner.withPropertyValues("management.health.redisearch.enabled:false")
			.run((context) -> assertThat(context).doesNotHaveBean(RediSearchHealthIndicator.class));
	}

	@TestConfiguration(proxyBeanMethods = false)
	@EnableConfigurationProperties(RediSearchProperties.class)
	static class Config {

	}

}
