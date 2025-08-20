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

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.redisearch.RediSearchAutoConfiguration;
import com.livk.context.redisearch.RediSearchConnectionFactory;
import org.springframework.boot.actuate.autoconfigure.health.CompositeHealthContributorConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * @author livk
 */
@SpringAutoService
@AutoConfiguration(after = { RediSearchAutoConfiguration.class })
@ConditionalOnClass({ RediSearchConnectionFactory.class, HealthIndicator.class })
@ConditionalOnBean(RediSearchConnectionFactory.class)
@ConditionalOnEnabledHealthIndicator("redisearch")
public class RediSearchHealthContributorAutoConfiguration
		extends CompositeHealthContributorConfiguration<RediSearchHealthIndicator, RediSearchConnectionFactory> {

	public RediSearchHealthContributorAutoConfiguration() {
		super(RediSearchHealthIndicator::new);
	}

	@Bean
	@ConditionalOnMissingBean(name = { "redisearchHealthIndicator", "redisearchHealthContributor" })
	public HealthContributor redisearchHealthContributor(
			Map<String, RediSearchConnectionFactory> rediSearchConnectionFactoryMap) {
		return createContributor(rediSearchConnectionFactoryMap);
	}

}
