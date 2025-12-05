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

package com.livk.autoconfigure.redisson;

import lombok.Data;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.MasterSlaveServersConfig;
import org.redisson.config.ReplicatedServersConfig;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PlaceholdersResolver;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.function.Consumer;

/**
 * The type Config properties.
 *
 * @author livk
 */
@Data
@ConfigurationProperties(RedissonProperties.PREFIX)
public class RedissonProperties {

	/**
	 * The constant PREFIX.
	 */
	public static final String PREFIX = "spring.redisson";

	private RedissonConfig config = new RedissonConfig();

	static RedissonProperties load(ConfigurableEnvironment environment) {
		Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(environment);
		ConfigurableConversionService conversionService = environment.getConversionService();
		PlaceholdersResolver resolver = new PropertySourcesPlaceholdersResolver(environment);
		Consumer<PropertyEditorRegistry> consumer = registry -> new RedissonPropertyEditorRegistrar()
			.registerCustomEditors(registry);
		Binder binder = new Binder(sources, resolver, conversionService, consumer);
		return binder.bind(RedissonProperties.PREFIX, RedissonProperties.class).orElse(new RedissonProperties());
	}

	public static class RedissonConfig extends Config {

		@Override
		@NestedConfigurationProperty
		public void setSingleServerConfig(SingleServerConfig singleServerConfig) {
			super.setSingleServerConfig(singleServerConfig);
		}

		@Override
		@NestedConfigurationProperty
		public void setMasterSlaveServersConfig(MasterSlaveServersConfig masterSlaveServersConfig) {
			super.setMasterSlaveServersConfig(masterSlaveServersConfig);
		}

		@Override
		@NestedConfigurationProperty
		public void setClusterServersConfig(ClusterServersConfig clusterServersConfig) {
			super.setClusterServersConfig(clusterServersConfig);
		}

		@Override
		@NestedConfigurationProperty
		public void setReplicatedServersConfig(ReplicatedServersConfig replicatedServersConfig) {
			super.setReplicatedServersConfig(replicatedServersConfig);
		}

		@Override
		@NestedConfigurationProperty
		public void setSentinelServersConfig(SentinelServersConfig sentinelServersConfig) {
			super.setSentinelServersConfig(sentinelServersConfig);
		}

		@Override
		@NestedConfigurationProperty
		public SingleServerConfig getSingleServerConfig() {
			return super.getSingleServerConfig();
		}

		@Override
		@NestedConfigurationProperty
		public MasterSlaveServersConfig getMasterSlaveServersConfig() {
			return super.getMasterSlaveServersConfig();
		}

		@Override
		@NestedConfigurationProperty
		public ClusterServersConfig getClusterServersConfig() {
			return super.getClusterServersConfig();
		}

		@Override
		@NestedConfigurationProperty
		public ReplicatedServersConfig getReplicatedServersConfig() {
			return super.getReplicatedServersConfig();
		}

		@Override
		@NestedConfigurationProperty
		public SentinelServersConfig getSentinelServersConfig() {
			return super.getSentinelServersConfig();
		}

	}

}
