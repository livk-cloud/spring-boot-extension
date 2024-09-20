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
import com.livk.context.redisearch.RedisSearchConnectionFactory;
import com.livk.context.redisearch.StringRediSearchTemplate;
import com.livk.context.redisearch.codec.RedisCodecs;
import com.redis.lettucemod.RedisModulesClient;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.resource.ClientResources;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * The type RediSearch autoconfiguration.
 *
 * @author livk
 */
@SpringAutoService
@AutoConfiguration
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
	 * Lettuce connection factory redis search connection factory.
	 * @param properties the properties
	 * @param clientResources the client resources
	 * @return the redis search connection factory
	 */
	@Bean(destroyMethod = "close")
	@ConditionalOnMissingBean
	public RedisSearchConnectionFactory lettuceConnectionFactory(RediSearchProperties properties,
			ClientResources clientResources) {
		return new LettuceModConnectionFactory(clientResources, properties);
	}

	/**
	 * Redi search template redi search template.
	 * @param factory the factory
	 * @param builder the builder
	 * @return the redi search template
	 */
	@Bean
	@ConditionalOnMissingBean
	public RediSearchTemplate<String, Object> rediSearchTemplate(RedisSearchConnectionFactory factory,
			Jackson2ObjectMapperBuilder builder) {
		RedisCodec<String, Object> redisCodec = RedisCodecs.json(builder.build(), String.class, Object.class);
		return new RediSearchTemplate<>(factory, redisCodec);
	}

	/**
	 * String redi search template string redi search template.
	 * @param factory the factory
	 * @return the string redi search template
	 */
	@Bean
	@ConditionalOnMissingBean
	public StringRediSearchTemplate stringRediSearchTemplate(RedisSearchConnectionFactory factory) {
		return new StringRediSearchTemplate(factory);
	}

}
