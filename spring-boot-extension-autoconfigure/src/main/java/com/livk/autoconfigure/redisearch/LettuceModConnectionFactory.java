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

package com.livk.autoconfigure.redisearch;

import com.livk.context.redisearch.RediSearchConnectionFactory;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.resource.ClientResources;
import lombok.experimental.Delegate;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author livk
 */
public class LettuceModConnectionFactory implements RediSearchConnectionFactory {

	@Delegate
	private final RediSearchConnectionFactory factory;

	private final RedisModulesFactory support;

	public LettuceModConnectionFactory(ClientResources resources, RediSearchProperties properties) {
		this.support = new RedisModulesFactory(properties);
		this.factory = init(resources);
	}

	private RediSearchConnectionFactory init(ClientResources resources) {
		AbstractRedisClient client = support.getClient(resources);
		return RediSearchConnectionFactory.create(client);
	}

	@Override
	public final <T> GenericObjectPoolConfig<T> getPoolConfig() {
		return support.getPoolConfig();
	}

}
