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

package com.livk.testcontainers;

import lombok.experimental.UtilityClass;
import org.testcontainers.utility.DockerImageName;

/**
 * Utility class providing Docker image name constants and factory methods.
 *
 * @author livk
 */
@UtilityClass
public class DockerImageNames {

	/** The latest tag constant. */
	public static final String LATEST_TAG = "latest";

	/** The MySQL Docker image name. */
	public static final String MYSQL_IMAGE = "mysql";

	/** The PostgreSQL Docker image name. */
	public static final String POSTGRES_IMAGE = "postgres";

	/** The Zookeeper Docker image name. */
	public static final String ZOOKEEPER_IMAGE = "zookeeper";

	/** The Redis Docker image name. */
	public static final String REDIS_IMAGE = "redis";

	/** The Redis Stack Docker image name. */
	public static final String REDIS_STACK_IMAGE = "redis/redis-stack-server";

	public static DockerImageName mysql() {
		return mysql(LATEST_TAG);
	}

	public static DockerImageName mysql(String tag) {
		return DockerImageName.parse(MYSQL_IMAGE).withTag(tag);
	}

	public static DockerImageName postgres() {
		return postgres(LATEST_TAG);
	}

	public static DockerImageName postgres(String tag) {
		return DockerImageName.parse(POSTGRES_IMAGE).withTag(tag);
	}

	public static DockerImageName zookeeper() {
		return zookeeper(LATEST_TAG);
	}

	public static DockerImageName zookeeper(String tag) {
		return DockerImageName.parse(ZOOKEEPER_IMAGE).withTag(tag);
	}

	public static DockerImageName redis() {
		return redis(LATEST_TAG);
	}

	public static DockerImageName redis(String tag) {
		return DockerImageName.parse(REDIS_IMAGE).withTag(tag);
	}

	public static DockerImageName redisStack() {
		return redisStack(LATEST_TAG);
	}

	public static DockerImageName redisStack(String tag) {
		return DockerImageName.parse(REDIS_STACK_IMAGE).withTag(tag);
	}

}
