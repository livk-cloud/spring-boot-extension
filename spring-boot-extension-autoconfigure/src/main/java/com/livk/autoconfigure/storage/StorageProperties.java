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

package com.livk.autoconfigure.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Storage properties.
 *
 * @author livk
 */
@Data
@ConfigurationProperties(StorageProperties.PREFIX)
public class StorageProperties {

	/**
	 * The constant PREFIX.
	 */
	public static final String PREFIX = "spring.storage";

	private String accessKey;

	private String secretKey;

	private String type;

	private String endpoint;

	private String region;

}
