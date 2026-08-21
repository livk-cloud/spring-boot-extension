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

package com.livk.commons.io;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

/**
 * Utility class for resource operations.
 *
 * @author livk
 * @see org.springframework.core.io.Resource
 * @see PathMatchingResourcePatternResolver
 * @deprecated use {@link ResourceScanner}
 */
@UtilityClass
@Deprecated(since = "2.1.1")
public class ResourceUtils extends org.springframework.util.ResourceUtils {

	/**
	 * Retrieves a single resource by location.
	 * @param location 资源地址
	 * @return the resource
	 */
	public Resource getResource(String location) {
		return ResourceScanner.getResource(location);
	}

	/**
	 * Retrieves multiple resources matching the location pattern.
	 * @param location 资源地址
	 * @return the resource[]
	 * @throws IOException the io exception
	 */
	public Resource[] getResources(String location) throws IOException {
		return ResourceScanner.getResources(location);
	}

}
