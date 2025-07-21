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

package com.livk.testcontainers.spring;

import com.livk.auto.service.annotation.SpringFactories;
import com.livk.testcontainers.DockerImageNames;
import com.livk.testcontainers.containers.ZookeeperContainer;
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;

/**
 * 需要根据{@link ConnectionDetails}设计一个ZookeeperConnectionDetails
 * <p>
 * 目前暂定使用ConnectionDetails
 *
 * @author livk
 * @see ConnectionDetails
 */
@SpringFactories(ConnectionDetailsFactory.class)
class ZookeeperContainerConnectionDetailsFactory
		extends ContainerConnectionDetailsFactory<ZookeeperContainer, ZookeeperConnectionDetails> {

	ZookeeperContainerConnectionDetailsFactory() {
		super(DockerImageNames.ZOOKEEPER_IMAGE);
	}

	@Override
	protected ZookeeperConnectionDetails getContainerConnectionDetails(
			ContainerConnectionSource<ZookeeperContainer> source) {
		return new ZookeeperContainerConnectionDetails(source);
	}

	private static final class ZookeeperContainerConnectionDetails
			extends ContainerConnectionDetails<ZookeeperContainer> implements ZookeeperConnectionDetails {

		private ZookeeperContainerConnectionDetails(ContainerConnectionSource<ZookeeperContainer> source) {
			super(source);
		}

		@Override
		public String getConnectString() {
			return String.format("%s:%s", getContainer().getHost(), getContainer().getMappedPort(2181));
		}

	}

}
