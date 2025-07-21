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
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;
import org.testcontainers.containers.MinIOContainer;

/**
 * @author livk
 */
@SpringFactories(ConnectionDetailsFactory.class)
class MinIOContainerConnectionDetailsFactory
		extends ContainerConnectionDetailsFactory<MinIOContainer, MinIOConnectionDetails> {

	MinIOContainerConnectionDetailsFactory() {
		super(DockerImageNames.MINIO_IMAGE);
	}

	@Override
	protected MinIOConnectionDetails getContainerConnectionDetails(ContainerConnectionSource<MinIOContainer> source) {
		return new MinIOContainerConnectionDetails(source);
	}

	private static final class MinIOContainerConnectionDetails extends ContainerConnectionDetails<MinIOContainer>
			implements MinIOConnectionDetails {

		private MinIOContainerConnectionDetails(ContainerConnectionSource<MinIOContainer> source) {
			super(source);
		}

		@Override
		public String getEndpoint() {
			return getContainer().getS3URL();
		}

		@Override
		public String getUserName() {
			return getContainer().getUserName();
		}

		@Override
		public String getPassword() {
			return getContainer().getPassword();
		}

	}

}
