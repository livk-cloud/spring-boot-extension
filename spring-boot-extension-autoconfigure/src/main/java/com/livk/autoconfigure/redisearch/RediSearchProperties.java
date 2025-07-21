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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * @author livk
 */
@Data
@ConfigurationProperties(prefix = "spring.redisearch")
public class RediSearchProperties {

	private Boolean ssl = false;

	private final Pool pool = new Pool();

	/**
	 * Database index used by the connection factory.
	 */
	private int database = 0;

	/**
	 * Redis server host.
	 */
	private String host = "localhost";

	/**
	 * Login username of the redis server.
	 */
	private String username;

	/**
	 * Login password of the redis server.
	 */
	private String password;

	/**
	 * Redis server port.
	 */
	private int port = 6379;

	/**
	 * timeout.
	 */
	private Duration timeout;

	/**
	 * Client name to be set on connections with CLIENT SETNAME.
	 */
	private String clientName;

	private Cluster cluster;

	/**
	 * Pool properties.
	 */
	@Data
	public static class Pool {

		/**
		 * Whether to enable the pool. Enabled automatically if "commons-pool2" is
		 * available. With Jedis, pooling is implicitly enabled in sentinel mode and this
		 * setting only applies to single node setup.
		 */
		private Boolean enabled = true;

		/**
		 * Maximum number of "idle" connections in the pool. Use a negative value to
		 * indicate an unlimited number of idle connections.
		 */
		private int maxIdle = 8;

		/**
		 * Target for the minimum number of idle connections to maintain in the pool. This
		 * setting only has an effect if both it and time between eviction runs are
		 * positive.
		 */
		private int minIdle = 0;

		/**
		 * Maximum number of connections that can be allocated by the pool at a given
		 * time. Use a negative value for no limit.
		 */
		private int maxActive = 8;

		/**
		 * Maximum amount of time a connection allocation should block before throwing an
		 * exception when the pool is exhausted. Use a negative value to block
		 * indefinitely.
		 */
		private Duration maxWait = Duration.ofMillis(-1);

	}

	/**
	 * Cluster properties.
	 */
	@Data
	public static class Cluster {

		private Boolean enabled;

		/**
		 * Comma-separated list of "host:port" pairs to bootstrap from. This represents an
		 * "initial" list of cluster nodes and is required to have at least one entry.
		 */
		private List<String> nodes;

		/**
		 * Maximum number of redirects to follow when executing commands across the
		 * cluster.
		 */
		private Integer maxRedirects;

	}

}
