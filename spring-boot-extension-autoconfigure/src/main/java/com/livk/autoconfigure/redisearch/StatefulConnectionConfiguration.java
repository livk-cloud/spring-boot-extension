/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.autoconfigure.redisearch;

import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Stateful connection configuration.
 *
 * @author livk
 */
@Configuration(proxyBeanMethods = false)
public class StatefulConnectionConfiguration {

	/**
	 * String connection stateful redis modules connection.
	 *
	 * @param stringGenericObjectPool the string generic object pool
	 * @return the stateful redis modules connection
	 * @throws Exception the exception
	 */
	@Bean(destroyMethod = "close")
	public StatefulRedisModulesConnection<String, String> stringConnection(GenericObjectPool<StatefulRedisModulesConnection<String, String>> stringGenericObjectPool) throws Exception {
		return stringGenericObjectPool.borrowObject();
	}

	/**
	 * Connection stateful redis modules connection.
	 *
	 * @param genericObjectPool the generic object pool
	 * @return the stateful redis modules connection
	 * @throws Exception the exception
	 */
	@Bean(destroyMethod = "close")
	public StatefulRedisModulesConnection<String, Object> connection(GenericObjectPool<StatefulRedisModulesConnection<String, Object>> genericObjectPool) throws Exception {
		return genericObjectPool.borrowObject();
	}
}
