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

package com.livk.autoconfigure.curator.actuator;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.curator.CuratorAutoConfiguration;
import org.apache.curator.framework.CuratorFramework;
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
 * The type Curator health contributor auto configuration.
 *
 * @author livk
 */
@SpringAutoService
@AutoConfiguration(after = { CuratorAutoConfiguration.class })
@ConditionalOnClass({ CuratorFramework.class, HealthIndicator.class })
@ConditionalOnBean(CuratorFramework.class)
@ConditionalOnEnabledHealthIndicator("curator")
public class CuratorHealthContributorAutoConfiguration
		extends CompositeHealthContributorConfiguration<CuratorHealthIndicator, CuratorFramework> {

	/**
	 * Instantiates a new Curator health contributor auto configuration.
	 */
	public CuratorHealthContributorAutoConfiguration() {
		super(CuratorHealthIndicator::new);
	}

	/**
	 * Curator health contributor health contributor.
	 * @param curatorFrameworkFactories the curator framework factories
	 * @return the health contributor
	 */
	@Bean
	@ConditionalOnMissingBean(name = { "curatorHealthIndicator", "curatorHealthContributor" })
	public HealthContributor curatorHealthContributor(Map<String, CuratorFramework> curatorFrameworkFactories) {
		return createContributor(curatorFrameworkFactories);
	}

}
