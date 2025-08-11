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

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.context.storage.AbstractStorageService;
import com.livk.context.storage.StorageTemplate;
import com.livk.context.storage.minio.MinioStorageService;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * The type Storage auto configuration.
 */
@SpringAutoService
@AutoConfiguration
@ConditionalOnClass(name = "com.livk.storage.marker.StorageMarker")
@EnableConfigurationProperties(StorageProperties.class)
public class StorageAutoConfiguration {

	/**
	 * template.
	 * @param storageService the abstract service
	 * @return the template
	 */
	@Bean(destroyMethod = "close")
	@ConditionalOnMissingBean
	@ConditionalOnBean(AbstractStorageService.class)
	public StorageTemplate storageTemplate(AbstractStorageService<?> storageService) {
		return new StorageTemplate(storageService);
	}

	/**
	 * The type Minio auto configuration.
	 */
	@AutoConfiguration
	@ConditionalOnClass(MinioClient.class)
	public static class MinioStorageAutoConfiguration {

		@Bean
		public MinioClient minioClient(StorageProperties properties) {
			return new MinioClient.Builder().endpoint(properties.getEndpoint())
				.credentials(properties.getAccessKey(), properties.getSecretKey())
				.region(properties.getRegion())
				.build();
		}

		/**
		 * Minio service minio service.
		 * @return the minio service
		 */
		@Bean(destroyMethod = "close")
		@ConditionalOnMissingBean(AbstractStorageService.class)
		public MinioStorageService minioService() {
			return new MinioStorageService();
		}

	}

}
