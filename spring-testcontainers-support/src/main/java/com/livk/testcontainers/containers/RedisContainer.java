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

package com.livk.testcontainers.containers;

import com.livk.testcontainers.DockerImageNames;
import org.testcontainers.utility.DockerImageName;

/**
 * @author livk
 * @deprecated 使用com.redis:testcontainers-redis
 */
@Deprecated(since = "1.4.5")
public class RedisContainer extends AbstractRedisContainer<RedisContainer> {

	public RedisContainer() {
		this(DockerImageNames.redis());
	}

	public RedisContainer(DockerImageName dockerImageName) {
		super(dockerImageName);
	}

}
