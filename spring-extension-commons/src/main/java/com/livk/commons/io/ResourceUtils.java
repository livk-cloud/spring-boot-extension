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
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 * 资源操作工具类
 *
 * @author livk
 * @see org.springframework.core.io.Resource
 * @see PathMatchingResourcePatternResolver
 */
@UtilityClass
public class ResourceUtils extends org.springframework.util.ResourceUtils {

	private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

	/**
	 * 获取单个资源
	 * @param location 资源地址
	 * @return the resource
	 */
	public Resource getResource(String location) {
		return resourceResolver.getResource(location);
	}

	/**
	 * 获取多个资源
	 * @param location 资源地址
	 * @return the resource[]
	 * @throws IOException the io exception
	 */
	public Resource[] getResources(String location) throws IOException {
		return resourceResolver.getResources(location);
	}

}
