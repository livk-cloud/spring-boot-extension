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

package com.livk.commons.io;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class ResourceScannerTests {

	static final String FILE_NAME = "input.json";

	@Test
	void getResourceReturnsMatchingFile() throws Exception {
		Resource resource = ResourceScanner.getResource(ResourceUtils.CLASSPATH_URL_PREFIX + FILE_NAME);
		Path expectedPath = new ClassPathResource(FILE_NAME).getFilePath();
		assertThat(resource.getFilePath()).isEqualTo(expectedPath);
	}

	@Test
	void getResourcesReturnsMatchingFiles() throws Exception {
		Path expectedPath = new ClassPathResource(FILE_NAME).getFilePath();
		Resource[] resources = ResourceScanner
			.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + FILE_NAME);
		assertThat(resources).hasSize(1).extracting(Resource::getFilePath).containsExactly(expectedPath);
	}

}
