/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.autoconfigure.redisearch;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.context.redisearch.RediSearchTemplate;
import com.livk.context.redisearch.StringRediSearchTemplate;
import com.livk.context.redisearch.codec.JacksonRedisCodec;
import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.cluster.RedisModulesClusterClient;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.resource.ClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

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
	 * @return the redis modules client
	 */
	@Bean(destroyMethod = "close")
	@ConditionalOnProperty(name = "spring.redisearch.cluster.enabled", havingValue = "false", matchIfMissing = true)
	public RedisModulesClient redisModulesClient(ClientResources clientResources, RediSearchProperties properties) {
		RedisURI redisURI = RediSearchSupport.create(properties);
		RedisModulesClient client = RedisModulesClient.create(clientResources, redisURI);
		ClientOptions.Builder builder = client.getOptions().mutate();
		client.setOptions(builder.build());
		return client;
	}

	/**
	 * Redis modules cluster client redis modules cluster client.
	 * @param clientResources the client resources
	 * @param properties the properties
	 * @return the redis modules cluster client
	 */
	@Bean(destroyMethod = "close")
	@ConditionalOnProperty(name = "spring.redisearch.cluster.enabled", havingValue = "true")
	public RedisModulesClusterClient redisModulesClusterClient(ClientResources clientResources,
			RediSearchProperties properties) {
		List<RedisURI> redisURIList = RediSearchSupport.createCluster(properties);
		RedisModulesClusterClient clusterClient = RedisModulesClusterClient.create(clientResources, redisURIList);
		ClusterClientOptions.Builder builder = ((ClusterClientOptions) clusterClient.getOptions()).mutate();
		if (properties.getCluster().getMaxRedirects() != null) {
			builder.maxRedirects(properties.getCluster().getMaxRedirects());
		}
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

	@Bean
	public RediSearchTemplate<String, Object> rediSearchTemplate(AbstractRedisClient client,
			GenericObjectPoolConfig<StatefulRedisModulesConnection<String, Object>> poolConfig,
			Jackson2ObjectMapperBuilder builder) {
		RediSearchTemplate<String, Object> template = new RediSearchTemplate<>(client, poolConfig);
		template.setRedisCodec(new JacksonRedisCodec<>(builder.build(), String.class, Object.class));
		return template;
	}

	@Bean
	public StringRediSearchTemplate stringRediSearchTemplate(AbstractRedisClient client,
			GenericObjectPoolConfig<StatefulRedisModulesConnection<String, String>> poolConfig) {
		return new StringRediSearchTemplate(client, poolConfig);
	}

}
