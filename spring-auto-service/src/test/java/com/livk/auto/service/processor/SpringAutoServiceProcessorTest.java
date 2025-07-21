/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.auto.service.processor;

import com.livk.auto.service.processor.autoconfigure.EnableAuto;
import com.livk.auto.service.processor.autoconfigure.SpringAutoContext;
import com.livk.auto.service.processor.autoconfigure.SpringContext;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.UrlResource;
import org.springframework.core.test.tools.SourceFile;
import org.springframework.core.test.tools.TestCompiler;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class SpringAutoServiceProcessorTest {

	@Test
	void testAutoConfiguration() {
		compile(SpringContext.class, "org.springframework.boot.autoconfigure.AutoConfiguration");
	}

	@Test
	void testEnableAuto() {
		compile(SpringAutoContext.class, EnableAuto.class.getName());
	}

	private void compile(Class<?> type, String annotationName) {
		TestCompiler.forSystem()
			.withProcessors(new SpringAutoServiceProcessor())
			.withSources(SourceFile.forTestClass(type))
			.compile(compiled -> {
				try {
					String location = String.format(SpringAutoServiceProcessor.LOCATION, annotationName);
					Enumeration<URL> resources = compiled.getClassLoader().getResources(location);
					List<String> configList = new ArrayList<>();
					for (URL url : Collections.list(resources)) {
						InputStream inputStream = new UrlResource(url).getInputStream();
						String[] arr = new String(FileCopyUtils.copyToByteArray(inputStream)).split("\n");
						configList.addAll(Arrays.stream(arr).map(String::trim).toList());
					}
					assertTrue(configList.contains(type.getName()));
				}
				catch (IOException ex) {
					throw new CompilationException(ex, SourceFile.forTestClass(type));
				}
			});
	}

}
