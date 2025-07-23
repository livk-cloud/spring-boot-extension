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
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class ResourceUtilsTests {

	@Test
	void getResource() throws IOException {
		String fileName = "input.json";
		Resource resource = ResourceUtils.getResource(ResourceUtils.CLASSPATH_URL_PREFIX + fileName);

		File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + fileName);

		assertThat(resource.getFile()).isEqualTo(file);

		Resource[] resources = ResourceUtils.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + fileName);
		for (Resource resourceObj : resources) {
			assertThat(resourceObj.getFile()).isEqualTo(file);
		}
	}

}
