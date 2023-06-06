/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.autoconfigure.ip2region;

import com.livk.commons.io.ResourceUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

/**
 * <p>
 * Ip2RegionProperties
 * </p>
 *
 * @author livk
 */
@Data
@ConfigurationProperties(Ip2RegionProperties.PREFIX)
public class Ip2RegionProperties {

	/**
	 * The constant PREFIX.
	 */
	public static final String PREFIX = "ip2region";

	private Boolean enabled = false;

	private String filePath = "classpath:ip/ip2region.xdb";

	/**
	 * Get file resource resource [ ].
	 *
	 * @return the resource [ ]
	 */
	public Resource getFileResource() {
		return ResourceUtils.getResource(filePath);
	}
}
