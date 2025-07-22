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

package com.livk.autoconfigure.dynamic;

import com.livk.autoconfigure.dynamic.exception.PrimaryNotFountException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author livk
 */
@Slf4j
@Data
@ConfigurationProperties(DynamicDatasourceProperties.PREFIX)
public class DynamicDatasourceProperties implements InitializingBean {

	/**
	 * The constant PREFIX.
	 */
	public static final String PREFIX = "spring.dynamic";

	private Map<String, DataSourceProperties> datasource;

	private String primary;

	@Override
	public void afterPropertiesSet() {
		if (StringUtils.hasText(primary)) {
			if (!datasource.containsKey(primary)) {
				throw new PrimaryNotFountException(
						primary + "The data source does not exist!\\nThe current data source does not exist:"
								+ datasource.keySet());
			}
		}
		else {
			throw new PrimaryNotFountException("The 'primary' datasource is missing!");
		}
	}

}
