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

import com.livk.auto.service.processor.factories.spring.SpringFactoryService;
import com.livk.auto.service.processor.factories.spring.SpringFactoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.UrlResource;
import org.springframework.core.test.tools.SourceFile;
import org.springframework.core.test.tools.TestCompiler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */

class SpringFactoriesProcessorTest {

	@Test
	void test() {
		compile(SpringFactoryServiceImpl.class, SpringFactoryService.class);
	}

	private void compile(Class<?> type, Class<?> factoryClass) {
		TestCompiler.forSystem()
			.withProcessors(new SpringFactoriesProcessor())
			.withSources(SourceFile.forTestClass(type))
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
					assertTrue(pro.containsKey(factoryClass.getName()));
					assertEquals(type.getName(), pro.get(factoryClass.getName()));
				}
				catch (IOException ex) {
					throw new CompilationException(ex, SourceFile.forTestClass(type));
				}
			});
	}

}
