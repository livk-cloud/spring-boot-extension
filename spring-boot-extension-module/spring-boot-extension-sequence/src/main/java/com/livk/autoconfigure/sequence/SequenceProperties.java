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

package com.livk.autoconfigure.sequence;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for distributed sequence generation.
 *
 * @author livk
 */
@Data
@ConfigurationProperties(SequenceProperties.PREFIX)
public class SequenceProperties {

	/**
	 * Configuration prefix.
	 */
	public static final String PREFIX = "spring.sequence";

	/**
	 * Sequence backend. Defaults to snowflake so the starter works without Redis or a
	 * database.
	 */
	private Type type = Type.SNOWFLAKE;

	/**
	 * Range step for database and Redis sequences.
	 */
	private int step = 1000;

	/**
	 * Initial value for database and Redis sequences. Issued ids start at
	 * {@code stepStart + 1}.
	 */
	private long stepStart = 0;

	/**
	 * Snowflake data center id.
	 */
	private long dataCenterId = 1;

	/**
	 * Snowflake machine id.
	 */
	private long machineId = 1;

	/**
	 * Optional business name used to expose a single {@code Sequence} bean.
	 */
	private String bizName;

	/**
	 * Supported sequence backends.
	 */
	public enum Type {

		/**
		 * Local snowflake ids.
		 */
		SNOWFLAKE,

		/**
		 * Database range ids.
		 */
		DB,

		/**
		 * Redis range ids.
		 */
		REDIS

	}

}
