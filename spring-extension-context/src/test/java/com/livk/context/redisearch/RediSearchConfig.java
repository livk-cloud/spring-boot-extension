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

package com.livk.context.redisearch;

import com.redis.lettucemod.RedisModulesClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.resource.ClientResources;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.properties.TestcontainersPropertySourceAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnectionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author livk
 */
@TestConfiguration
@Import({ ServiceConnectionAutoConfiguration.class, TestcontainersPropertySourceAutoConfiguration.class })
class RediSearchConfig {

	@Bean(destroyMethod = "close")
	public RediSearchConnectionFactory rediSearchConnectionFactory(@Value("${redisearch.host}") String host,
			@Value("${redisearch.port}") Integer port) {
		ClientResources resources = ClientResources.builder().build();
		RedisModulesClient client = RedisModulesClient.create(resources, RedisURI.create(host, port));
		return FactoryProxySupport.newProxy(client);
	}

}
