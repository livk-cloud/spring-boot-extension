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
 * @author livk
 */
@UtilityClass
public class DockerImageNames {

	public static final String LATEST_TAG = "latest";

	public static final String MYSQL_IMAGE = "mysql";

	public static final String POSTGRES_IMAGE = "postgres";

	public static final String ZOOKEEPER_IMAGE = "zookeeper";

	public static final String REDIS_IMAGE = "redis";

	public static final String REDIS_STACK_IMAGE = "redis/redis-stack-server";

	public static final String CLICKHOUSE_IMAGE = "clickhouse/clickhouse-server";

	public static final String KAFKA_IMAGE = "apache/kafka";

	public static final String RABBITMQ_IMAGE = "rabbitmq";

	public static final String PULSAR_IMAGE = "apachepulsar/pulsar";

	public static final String MINIO_IMAGE = "minio/minio";

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

	public static DockerImageName clickhouse() {
		return clickhouse(LATEST_TAG);
	}

	public static DockerImageName clickhouse(String tag) {
		return DockerImageName.parse(CLICKHOUSE_IMAGE).withTag(tag);
	}

	public static DockerImageName kafka() {
		return kafka(LATEST_TAG);
	}

	public static DockerImageName kafka(String tag) {
		return DockerImageName.parse(KAFKA_IMAGE).withTag(tag);
	}

	public static DockerImageName rabbitmq() {
		return rabbitmq(LATEST_TAG);
	}

	public static DockerImageName rabbitmq(String tag) {
		return DockerImageName.parse(RABBITMQ_IMAGE).withTag(tag);
	}

	public static DockerImageName pulsar() {
		return pulsar(LATEST_TAG);
	}

	public static DockerImageName pulsar(String tag) {
		return DockerImageName.parse(PULSAR_IMAGE).withTag(tag);
	}

	public static DockerImageName minio() {
		return minio(LATEST_TAG);
	}

	public static DockerImageName minio(String tag) {
		return DockerImageName.parse(MINIO_IMAGE).withTag(tag);
	}

}
