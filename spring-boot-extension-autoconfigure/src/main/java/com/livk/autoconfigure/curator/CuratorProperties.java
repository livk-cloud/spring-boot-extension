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

package com.livk.autoconfigure.curator;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * CuratorProperties
 * </p>
 *
 * @author livk
 */
@Data
@ConfigurationProperties(CuratorProperties.PREFIX)
public class CuratorProperties {

	/**
	 * The constant PREFIX.
	 */
	public static final String PREFIX = "spring.zookeeper.curator";

	/**
	 * Connection string to the Zookeeper cluster.
	 */
	private String connectString = "localhost:2181";

	/**
	 * Initial amount of time to wait between retries.
	 */
	private Integer baseSleepTimeMs = 50;

	/**
	 * Max number of times to retry.
	 */
	private Integer maxRetries = 10;

	/**
	 * Max time in ms to sleep on each retry.
	 */
	private Integer maxSleepMs = 500;

	/**
	 * Wait time to block on connection to Zookeeper.
	 */
	private Integer blockUntilConnectedWait = 10;

	/**
	 * The unit of time related to blocking on connection to Zookeeper.
	 */
	private TimeUnit blockUntilConnectedUnit = TimeUnit.SECONDS;

	/**
	 * The configured/negotiated session timeout in milliseconds. Please refer to
	 * <a href='https://cwiki.apache.org/confluence/display/CURATOR/TN14'>Curator's Tech
	 * Note 14</a> to understand how Curator implements connection sessions.
	 *
	 * @see <a href='https://cwiki.apache.org/confluence/display/CURATOR/TN14'>Curator's
	 * Tech Note 14</a>
	 */
	@DurationUnit(ChronoUnit.MILLIS)
	private Duration sessionTimeout = Duration.of(60, ChronoUnit.SECONDS);

	/**
	 * The configured connection timeout in milliseconds.
	 */
	@DurationUnit(ChronoUnit.MILLIS)
	private Duration connectionTimeout = Duration.of(15, ChronoUnit.SECONDS);

}
