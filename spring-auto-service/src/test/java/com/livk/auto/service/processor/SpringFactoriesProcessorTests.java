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

package com.livk.auto.service.processor;

import com.livk.auto.service.processor.factories.spring.AbstractSpringFactoryService;
import com.livk.auto.service.processor.factories.spring.SpringFactoryService;
import com.livk.auto.service.processor.factories.spring.SpringFactoryServiceExtImpl;
import com.livk.auto.service.processor.factories.spring.SpringFactoryServiceImpl;
import com.livk.auto.service.processor.factories.spring.SpringFactoryServiceImpl2;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.UrlResource;
import org.springframework.core.test.tools.SourceFile;
import org.springframework.core.test.tools.TestCompiler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */

class SpringFactoriesProcessorTests {

	@Test
	void springFactoriesFileContainsServiceImplInferredFromSingleInterface() {
		compile(List.of(SpringFactoryServiceImpl.class), SpringFactoryService.class,
				SpringFactoryServiceImpl.class.getName());
	}

	@Test
	void springFactoriesFileContainsMultipleImplementations() {
		compile(List.of(SpringFactoryServiceImpl.class, SpringFactoryServiceImpl2.class), SpringFactoryService.class,
				SpringFactoryServiceImpl.class.getName(), SpringFactoryServiceImpl2.class.getName());
	}

	@Test
	void springFactoriesFileInfersProviderFromSuperclassWhenNoSingleInterface() {
		compile(List.of(SpringFactoryServiceExtImpl.class), AbstractSpringFactoryService.class,
				SpringFactoryServiceExtImpl.class.getName());
	}

	@Test
	void springFactoriesFileUsesContinuationFormatForMultipleImplementations() {
		SourceFile[] sources = List.of(SpringFactoryServiceImpl.class, SpringFactoryServiceImpl2.class)
			.stream()
			.map(SourceFile::forTestClass)
			.toArray(SourceFile[]::new);
		TestCompiler.forSystem()
			.withProcessors(new SpringFactoriesProcessor())
			.withSources(sources)
			.compile(compiled -> {
				try {
					String content = null;
					Enumeration<URL> resources = compiled.getClassLoader()
						.getResources(SpringFactoriesProcessor.SPRING_LOCATION);
					for (URL url : Collections.list(resources)) {
						String candidate = new String(url.openStream().readAllBytes(), StandardCharsets.UTF_8);
						if (candidate.contains(SpringFactoryService.class.getName())) {
							content = candidate;
							break;
						}
					}
					assertThat(content).isNotNull()
						.contains(SpringFactoryService.class.getName() + "=\\")
						.contains(",\\")
						.contains(SpringFactoryServiceImpl.class.getName())
						.contains(SpringFactoryServiceImpl2.class.getName());
				}
				catch (IOException ex) {
					throw new CompilationException(ex, sources[0]);
				}
			});
	}

	private void compile(List<Class<?>> types, Class<?> factoryClass, String... expectedImpls) {
		SourceFile[] sources = types.stream().map(SourceFile::forTestClass).toArray(SourceFile[]::new);
		TestCompiler.forSystem()
			.withProcessors(new SpringFactoriesProcessor())
			.withSources(sources)
			.compile(compiled -> {
				try {
					Enumeration<URL> resources = compiled.getClassLoader()
						.getResources(SpringFactoriesProcessor.SPRING_LOCATION);
					Properties pro = new Properties();
					for (URL url : Collections.list(resources)) {
						InputStream inputStream = new UrlResource(url).getInputStream();
						Properties properties = new Properties();
						properties.load(inputStream);
						pro.putAll(properties);
					}
					String key = factoryClass.getName();

					assertThat(pro).containsKey(key);
					String value = pro.get(key).toString();
					assertThat(value).contains(expectedImpls);

				}
				catch (IOException ex) {
					throw new CompilationException(ex, sources[0]);
				}
			});
	}

}
