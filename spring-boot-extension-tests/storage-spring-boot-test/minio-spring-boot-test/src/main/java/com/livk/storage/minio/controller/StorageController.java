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

package com.livk.storage.minio.controller;

import com.livk.context.storage.StorageTemplate;
import com.livk.commons.SpringContextHolder;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author livk
 */
@Slf4j
@RestController
@RequestMapping("storage")
@RequiredArgsConstructor
public class StorageController {

	public final StorageTemplate template;

	@PostConstruct
	public void init() {
		template.removeBucketAndObj("test");
	}

	@GetMapping("test")
	public HttpEntity<String> test() throws IOException {
		log.info("minioClient: {}", SpringContextHolder.getBean(MinioClient.class));
		InputStream stream = new ClassPathResource("test.jpg").getInputStream();
		template.createBucket("test");
		if (!template.exist("test", "test.jpg")) {
			template.upload("test", "test.jpg", stream);
		}
		String url = template.getExternalLink("test", "test.jpg");
		return ResponseEntity.ok(url);
	}

}
