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

package com.livk.autoconfigure.redisearch;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.redisearch.codec.JdkRedisCodec;
import com.livk.autoconfigure.redisearch.customizer.ClientOptionsBuilderCustomizer;
import com.livk.autoconfigure.redisearch.customizer.ClientResourcesBuilderCustomizer;
import com.livk.autoconfigure.redisearch.customizer.ClusterClientOptionsBuilderCustomizer;
import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.cluster.RedisModulesClusterClient;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.resource.ClientResources;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

/**
 * The type RediSearch autoconfiguration.
 *
 * @author livk
 */
@SpringAutoService
@Configuration(proxyBeanMethods = false, enforceUniqueMethods = false)
@ConditionalOnClass(RedisModulesClient.class)
@EnableConfigurationProperties(RediSearchProperties.class)
public class RediSearchAutoConfiguration {

	/**
	 * Client resources client resources.
	 * @param providers the providers
	 * @return the client resources
	 */
	@Bean(destroyMethod = "shutdown")
	public ClientResources clientResources(ObjectProvider<ClientResourcesBuilderCustomizer> providers) {
		ClientResources.Builder builder = ClientResources.builder();
		providers.orderedStream().forEach(customizer -> customizer.customize(builder));
		return builder.build();
	}

	/**
	 * Redis modules client redis modules client.
	 * @param clientResources the client resources
	 * @param properties the properties
	 * @param providers the providers
	 * @return the redis modules client
	 */
	@Bean(destroyMethod = "close")
	@ConditionalOnProperty(name = "spring.redisearch.cluster.enabled", havingValue = "false", matchIfMissing = true)
	public RedisModulesClient redisModulesClient(ClientResources clientResources, RediSearchProperties properties,
			ObjectProvider<ClientOptionsBuilderCustomizer> providers) {
		RedisURI redisURI = RediSearchSupport.create(properties);
		RedisModulesClient client = RedisModulesClient.create(clientResources, redisURI);
		ClientOptions.Builder builder = client.getOptions().mutate();
		providers.orderedStream().forEach(customizer -> customizer.customize(builder));
		client.setOptions(builder.build());
		return client;
	}

	/**
	 * Redis modules cluster client redis modules cluster client.
	 * @param clientResources the client resources
	 * @param properties the properties
	 * @param providers the providers
	 * @return the redis modules cluster client
	 */
	@Bean(destroyMethod = "close")
	@ConditionalOnProperty(name = "spring.redisearch.cluster.enabled", havingValue = "true")
	public RedisModulesClusterClient redisModulesClusterClient(ClientResources clientResources,
			RediSearchProperties properties, ObjectProvider<ClusterClientOptionsBuilderCustomizer> providers) {
		List<RedisURI> redisURIList = RediSearchSupport.createCluster(properties);
		RedisModulesClusterClient clusterClient = RedisModulesClusterClient.create(clientResources, redisURIList);
		ClusterClientOptions.Builder builder = ((ClusterClientOptions) clusterClient.getOptions()).mutate();
		if (properties.getCluster().getMaxRedirects() != null) {
			builder.maxRedirects(properties.getCluster().getMaxRedirects());
		}
		providers.orderedStream().forEach(customizer -> customizer.customize(builder));
		clusterClient.setOptions(builder.build());
		return clusterClient;
	}

	/**
	 * Pool config generic object pool config.
	 * @param properties the properties
	 * @return the generic object pool config
	 */
	@Bean
	@ConditionalOnMissingBean
	public GenericObjectPoolConfig<?> poolConfig(RediSearchProperties properties) {
		return RediSearchSupport.withPoolConfig(properties);
	}

	/**
	 * The type Redi search pool configuration.
	 */
	@AutoConfiguration
	@Import(StatefulConnectionConfiguration.class)
	@ConditionalOnProperty(name = "spring.redisearch.cluster.enabled", havingValue = "false", matchIfMissing = true)
	public static class RediSearchPoolConfiguration {

		/**
		 * String generic object pool generic object pool.
		 * @param redisModulesClient the redis modules client
		 * @param config the config
		 * @return the generic object pool
		 */
		@Bean(destroyMethod = "close")
		public GenericObjectPool<StatefulRedisModulesConnection<String, String>> stringGenericObjectPool(
				RedisModulesClient redisModulesClient,
				GenericObjectPoolConfig<StatefulRedisModulesConnection<String, String>> config) {
			return RediSearchSupport.pool(redisModulesClient::connect, config);
		}

		/**
		 * Generic object pool generic object pool.
		 * @param redisModulesClient the redis modules client
		 * @param config the config
		 * @return the generic object pool
		 */
		@Bean(destroyMethod = "close")
		public GenericObjectPool<StatefulRedisModulesConnection<String, Object>> genericObjectPool(
				RedisModulesClient redisModulesClient,
				GenericObjectPoolConfig<StatefulRedisModulesConnection<String, Object>> config) {
			return RediSearchSupport.pool(() -> redisModulesClient.connect(new JdkRedisCodec()), config);
		}

	}

	/**
	 * The type Redi search cluster pool configuration.
	 */
	@AutoConfiguration
	@Import(StatefulConnectionConfiguration.class)
	@ConditionalOnProperty(name = "spring.redisearch.cluster.enabled", havingValue = "true")
	public static class RediSearchClusterPoolConfiguration {

		/**
		 * String generic object pool generic object pool.
		 * @param redisModulesClusterClient the redis modules cluster client
		 * @param config the config
		 * @return the generic object pool
		 */
		@Bean(destroyMethod = "close")
		public GenericObjectPool<StatefulRedisModulesConnection<String, String>> stringGenericObjectPool(
				RedisModulesClusterClient redisModulesClusterClient,
				GenericObjectPoolConfig<StatefulRedisModulesConnection<String, String>> config) {
			return RediSearchSupport.pool(redisModulesClusterClient::connect, config);
		}

		/**
		 * Generic object pool generic object pool.
		 * @param redisModulesClusterClient the redis modules cluster client
		 * @param config the config
		 * @return the generic object pool
		 */
		@Bean(destroyMethod = "close")
		public GenericObjectPool<StatefulRedisModulesConnection<String, Object>> genericObjectPool(
				RedisModulesClusterClient redisModulesClusterClient,
				GenericObjectPoolConfig<StatefulRedisModulesConnection<String, Object>> config) {
			return RediSearchSupport.pool(() -> redisModulesClusterClient.connect(new JdkRedisCodec()), config);
		}

	}

}
