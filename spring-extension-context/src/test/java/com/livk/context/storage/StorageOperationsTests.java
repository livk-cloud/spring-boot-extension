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
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * StorageOperationsTests
 * </p>
 *
 * @author livk
 * @date 2025/8/11
 */
@SpringJUnitConfig(StorageOperationsTests.Config.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class StorageOperationsTests {

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
	MinioStorageService storageService;

	@Test
	void exist() {
		assertThat(storageService.exist("exist")).isFalse();

		storageService.createBucket("exist");
		assertThat(storageService.exist("exist")).isTrue();
	}

	@Test
	void createBucket() {
		storageService.createBucket("create-bucket");

		assertThat(storageService.exist("create-bucket")).isTrue();
		assertThat(storageService.allBuckets()).contains("create-bucket");
	}

	@Test
	void allBuckets() {
		storageService.createBucket("all-buckets1");
		storageService.createBucket("all-buckets2");
		assertThat(storageService.allBuckets()).contains("all-buckets1", "all-buckets2");
	}

	@Test
	void removeBucket() {
		assertThat(storageService.exist("remove-bucket")).isFalse();
		storageService.createBucket("remove-bucket");
		assertThat(storageService.exist("remove-bucket")).isTrue();

		storageService.removeBucket("remove-bucket");
		assertThat(storageService.exist("remove-bucket")).isFalse();
	}

	@Test
	void testExist() throws Exception {
		ClassPathResource resource = new ClassPathResource("qrCode.png");
		storageService.createBucket("test-exist");
		storageService.upload("test-exist", resource.getFilename(), resource.getInputStream());
		assertThat(storageService.exist("test-exist", resource.getFilename())).isTrue();

		storageService.removeObj("test-exist", resource.getFilename());
		assertThat(storageService.exist("test-exist", resource.getFilename())).isFalse();
	}

	@Test
	void upload() throws Exception {
		ClassPathResource resource = new ClassPathResource("qrCode.png");
		storageService.createBucket("upload");
		assertThat(storageService.exist("upload", resource.getFilename())).isFalse();
		storageService.upload("upload", resource.getFilename(), resource.getInputStream());
		assertThat(storageService.exist("upload", resource.getFilename())).isTrue();
	}

	@Test
	void download() throws Exception {
		ClassPathResource resource = new ClassPathResource("qrCode.png");
		storageService.createBucket("download");
		storageService.upload("download", resource.getFilename(), resource.getInputStream());
		assertThat(storageService.exist("download", resource.getFilename())).isTrue();

		InputStream stream = storageService.download("download", resource.getFilename());
		QrCodeManager manager = GoogleQrCodeManager.of(JsonMapper.builder().build());

		assertThat(manager.parser(stream)).isNotBlank();
	}

	@Test
	void removeObj() throws Exception {
		ClassPathResource resource = new ClassPathResource("qrCode.png");
		storageService.createBucket("remove-obj");
		assertThat(storageService.exist("remove-obj", resource.getFilename())).isFalse();
		storageService.upload("remove-obj", resource.getFilename(), resource.getInputStream());
		assertThat(storageService.exist("remove-obj", resource.getFilename())).isTrue();
		storageService.removeObj("remove-obj", resource.getFilename());
		assertThat(storageService.exist("remove-obj", resource.getFilename())).isFalse();
	}

	@Test
	void removeObjs() throws Exception {
		ClassPathResource resource = new ClassPathResource("qrCode.png");
		storageService.createBucket("remove-objs");
		for (int i = 0; i < 3; i++) {
			storageService.upload("remove-objs", resource.getFilename() + i, resource.getInputStream());
		}

		assertThat(storageService.getAllObj("remove-objs")).hasSize(3);

		storageService.removeObjs("remove-objs", resource.getFilename() + 0, resource.getFilename() + 1,
				resource.getFilename() + 2);
		assertThat(storageService.getAllObj("remove-objs")).isEmpty();
	}

	@Test
	void getStrUrl() throws Exception {
		ClassPathResource resource = new ClassPathResource("qrCode.png");
		storageService.createBucket("get-str-url");
		storageService.upload("get-str-url", resource.getFilename(), resource.getInputStream());

		assertThat(storageService.getStrUrl("get-str-url", resource.getFilename())).isNotBlank();
	}

	@Test
	void testGetStrUrl() throws Exception {
		ClassPathResource resource = new ClassPathResource("qrCode.png");
		storageService.createBucket("test-get-str-url");
		storageService.upload("test-get-str-url", resource.getFilename(), resource.getInputStream());

		assertThat(storageService.getStrUrl("test-get-str-url", resource.getFilename())).isNotBlank();
	}

	@Test
	void getAllObj() throws Exception {
		storageService.createBucket("get-all-obj");
		assertThat(storageService.getAllObj("get-all-obj")).isEmpty();

		ClassPathResource resource = new ClassPathResource("qrCode.png");
		storageService.upload("get-all-obj", resource.getFilename(), resource.getInputStream());
		assertThat(storageService.getAllObj("get-all-obj")).hasSize(1);
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

	}

}
