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

package com.livk.context.storage;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.livk.context.qrcode.QrCodeManager;
import com.livk.context.qrcode.support.GoogleQrCodeManager;
import com.livk.context.storage.minio.MinioStorageService;
import com.livk.testcontainers.DockerImageNames;
import io.minio.MinioClient;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.properties.TestcontainersPropertySourceAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.testcontainers.service.connection.ServiceConnectionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.StringUtils;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * StorageTemplateTests
 * </p>
 *
 * @author livk
 * @date 2025/8/11
 */
@SpringJUnitConfig(StorageTemplateTests.Config.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class StorageTemplateTests {

	@Container
	@ServiceConnection
	static final MinIOContainer minio = new MinIOContainer(DockerImageNames.minio()).withUserName("admin")
		.withPassword("1375632510")
		.withExposedPorts(9000);

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("storage.access-key", minio::getUserName);
		registry.add("storage.secret-key", minio::getPassword);
		registry.add("storage.endpoint", minio::getS3URL);
	}

	@Autowired
	StorageTemplate template;

	@Test
	void removeBucketAndObj() throws Exception {
		ClassPathResource resource = new ClassPathResource("qrCode.png");
		template.createBucket("test");
		for (int i = 0; i < 3; i++) {
			template.upload("test", resource.getFilename() + i, resource.getInputStream());
		}

		assertThat(template.getAllObj("test")).hasSize(3);
		template.removeBucketAndObj("test");

		assertThat(template.exist("test")).isFalse();
	}

	@Test
	void initBucket() {
		template.initBucket("init-bucket1", "init-bucket-2", "init-bucket-3");

		assertThat(template.exist("init-bucket1")).isTrue();
		assertThat(template.exist("init-bucket-2")).isTrue();
		assertThat(template.exist("init-bucket-3")).isTrue();

		assertThat(template.allBuckets()).contains("init-bucket1", "init-bucket-2", "init-bucket-3");
	}

	@Test
	void download() throws Exception {
		ClassPathResource resource = new ClassPathResource("qrCode.png");
		template.createBucket("download");
		for (int i = 0; i < 3; i++) {
			template.upload("download", resource.getFilename() + i, resource.getInputStream());
		}

		assertThat(template.getAllObj("download")).hasSize(3);

		assertThat(template.download("download", resource.getFilename() + 0, resource.getFilename() + 1,
				resource.getFilename() + 2))
			.hasSize(3)
			.containsOnlyKeys(resource.getFilename() + 0, resource.getFilename() + 1, resource.getFilename() + 2)
			.hasValueSatisfying(new QrCodeCondition());
	}

	@Test
	void getExternalLink() throws Exception {
		ClassPathResource resource = new ClassPathResource("qrCode.png");
		template.createBucket("get-external-link");
		template.upload("get-external-link", resource.getFilename(), resource.getInputStream());
		assertThat(template.getExternalLink("get-external-link", resource.getFilename())).isNotBlank();
	}

	static class QrCodeCondition extends Condition<InputStream> {

		QrCodeManager manager = GoogleQrCodeManager.of(JsonMapper.builder().build());

		@Override
		public boolean matches(InputStream value) {
			String parser = manager.parser(value);
			return StringUtils.hasText(parser);
		}

	}

	@TestConfiguration
	@Import({ ServiceConnectionAutoConfiguration.class, TestcontainersPropertySourceAutoConfiguration.class })
	static class Config {

		@Bean(destroyMethod = "close")
		public MinioClient minioClient(@Value("${storage.endpoint}") String endpoint,
				@Value("${storage.access-key}") String accessKey, @Value("${storage.secret-key}") String secretKey) {
			return MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
		}

		@Bean
		public MinioStorageService minioStorageService() {
			return new MinioStorageService();
		}

		@Bean
		public StorageTemplate storageTemplate(MinioStorageService minioStorageService) {
			return new StorageTemplate(minioStorageService);
		}

	}

}
