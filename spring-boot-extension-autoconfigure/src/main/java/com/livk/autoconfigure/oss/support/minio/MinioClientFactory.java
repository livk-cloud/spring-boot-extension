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

package com.livk.autoconfigure.oss.support.minio;

import com.livk.autoconfigure.oss.factory.OSSClientFactory;
import io.minio.MinioClient;

/**
 * The type Minio client factory.
 *
 * @author livk
 */
public class MinioClientFactory implements OSSClientFactory<MinioClient> {

	@Override
	public MinioClient instance(String endpoint, String accessKey, String secretKey, String region) {
		return new MinioClient.Builder().endpoint(endpoint).credentials(accessKey, secretKey).region(region).build();
	}

	@Override
	public String name() {
		return "minio";
	}

}
