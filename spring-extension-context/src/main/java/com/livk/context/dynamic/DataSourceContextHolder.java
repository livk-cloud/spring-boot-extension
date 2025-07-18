/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.context.dynamic;

import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;
import org.springframework.util.StringUtils;

/**
 * <p>
 * DataSourceContextHolder
 * </p>
 *
 * @author livk
 */
public class DataSourceContextHolder {

	private static final ThreadLocal<String> datasourceHolder = new NamedThreadLocal<>("datasource context");

	private static final ThreadLocal<String> inheritableDatasourceHolder = new NamedInheritableThreadLocal<>(
			"inheritable datasource context");

	/**
	 * Switch data source.
	 * @param datasource the datasource
	 */
	public static void switchDataSource(String datasource) {
		switchDataSource(datasource, false);
	}

	/**
	 * Switch data source.
	 * @param datasource the datasource
	 * @param inheritable the inheritable
	 */
	public static void switchDataSource(String datasource, boolean inheritable) {
		if (StringUtils.hasText(datasource)) {
			if (inheritable) {
				inheritableDatasourceHolder.set(datasource);
				datasourceHolder.remove();
			}
			else {
				datasourceHolder.set(datasource);
				inheritableDatasourceHolder.remove();
			}
		}
		else {
			clear();
		}
	}

	/**
	 * Get data source.
	 * @return the data source
	 */
	public static String getDataSource() {
		String datasource = datasourceHolder.get();
		return StringUtils.hasText(datasource) ? datasource : inheritableDatasourceHolder.get();
	}

	/**
	 * Clear.
	 */
	public static void clear() {
		datasourceHolder.remove();
		inheritableDatasourceHolder.remove();
	}

}
