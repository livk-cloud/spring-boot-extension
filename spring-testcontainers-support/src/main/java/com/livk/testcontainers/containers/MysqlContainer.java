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

package com.livk.testcontainers.containers;

import com.livk.testcontainers.DockerImageNames;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.images.builder.Transferable;

/**
 * @author livk
 */
public class MysqlContainer extends MySQLContainer<MysqlContainer> {

	public MysqlContainer() {
		super(DockerImageNames.mysql());
		addExposedPorts(3306);

	}

	/**
	 * <a href="https://github.com/testcontainers/testcontainers-java/pull/10185">MySql
	 * 9.3不兼容问题</a>
	 */
	@Override
	protected void configure() {
		super.configure();
		optionallyMapResourceParameterAsVolume("TC_MY_CNF", "/etc/mysql/conf.d", "mysql-conf",
				Transferable.DEFAULT_DIR_MODE);
	}

}
