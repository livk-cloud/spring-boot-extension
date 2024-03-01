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

package com.livk.testcontainers;

import org.testcontainers.utility.DockerImageName;

/**
 * <p>
 * DockerImageNames
 * </p>
 *
 * @author livk
 * @date 2024/3/1
 */
public class DockerImageNames {

	public static final String LATEST_TAG = "latest";

	public static DockerImageName mysql() {
		return mysql(LATEST_TAG);
	}

	public static DockerImageName mysql(String tag) {
		return DockerImageName.parse("mysql").withTag(tag);
	}

	public static DockerImageName postgres() {
		return postgres(LATEST_TAG);
	}

	public static DockerImageName postgres(String tag) {
		return DockerImageName.parse("postgres").withTag(tag);
	}

	public static DockerImageName zookeeper() {
		return zookeeper(LATEST_TAG);
	}

	public static DockerImageName zookeeper(String tag) {
		return DockerImageName.parse("zookeeper").withTag(tag);
	}

	public static DockerImageName redis() {
		return redis(LATEST_TAG);
	}

	public static DockerImageName redis(String tag) {
		return DockerImageName.parse("redis").withTag(tag);
	}

	public static DockerImageName redisStack() {
		return redisStack(LATEST_TAG);
	}

	public static DockerImageName redisStack(String tag) {
		return DockerImageName.parse("redis/redis-stack").withTag(tag);
	}

	public static DockerImageName clickhouse() {
		return clickhouse(LATEST_TAG);
	}

	public static DockerImageName clickhouse(String tag) {
		return DockerImageName.parse("clickhouse/clickhouse-server").withTag(tag);
	}
}
