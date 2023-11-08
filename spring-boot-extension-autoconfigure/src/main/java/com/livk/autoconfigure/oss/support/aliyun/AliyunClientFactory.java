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

package com.livk.autoconfigure.oss.support.aliyun;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.livk.autoconfigure.oss.client.OSSClientFactory;

/**
 * The type Aliyun client factory.
 *
 * @author livk
 */
public class AliyunClientFactory implements OSSClientFactory<OSS> {

	@Override
	public OSS instance(String endpoint, String accessKey, String secretKey, String region) {
		CredentialsProvider provider = new DefaultCredentialProvider(accessKey, secretKey);
		return OSSClientBuilder.create()
			.endpoint(endpoint)
			.credentialsProvider(provider)
			.region(region)
			.build();
	}

	@Override
	public String name() {
		return "aliyun";
	}
}
