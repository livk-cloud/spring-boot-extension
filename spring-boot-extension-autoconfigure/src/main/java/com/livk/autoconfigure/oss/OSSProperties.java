/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.autoconfigure.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.net.URI;

/**
 * The type Oss properties.
 *
 * @author livk
 */
@Data
@ConfigurationProperties(OSSProperties.PREFIX)
public class OSSProperties {

	/**
	 * The constant PREFIX.
	 */
	public static final String PREFIX = "spring.oss";

	private String accessKey;

	private String secretKey;

	private String type;

	private String endpoint;

	private String region;

}
