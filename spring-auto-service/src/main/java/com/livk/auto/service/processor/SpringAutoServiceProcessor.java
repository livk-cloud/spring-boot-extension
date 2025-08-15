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

import com.google.auto.service.AutoService;
import com.livk.auto.service.annotation.SpringAutoService;

import javax.annotation.processing.Processor;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author livk
 */
@AutoService(Processor.class)
public class SpringAutoServiceProcessor extends CustomizeAbstractProcessor {

	private static final Class<SpringAutoService> SUPPORT_CLASS = SpringAutoService.class;

	private static final String AUTOCONFIGURATION = "org.springframework.boot.autoconfigure.AutoConfiguration";

	static final String LOCATION = "META-INF/spring/%s.imports";

	@Override
	protected Class<? extends Annotation> getSupportedAnnotation() {
		return SUPPORT_CLASS;
	}

	@Override
	protected void generateConfigFiles() {
		for (Map.Entry<String, Collection<String>> entity : processorMap.asMap().entrySet()) {
			String resourceFile = String.format(LOCATION, entity.getKey());
			Set<String> exitImports = this.readFromResource(resourceFile);
			if (!exitImports.isEmpty()) {
				log("Existing service entries: " + exitImports);
			}
			Set<String> allImports = Stream.concat(exitImports.stream(), entity.getValue().stream())
				.collect(Collectors.toSet());
			this.writeFile(allImports, resourceFile);
		}
	}

	@Override
	protected Set<TypeElement> elseElement(Element element) {
		return Set.of(elements.getTypeElement(AUTOCONFIGURATION));
	}

	private Set<String> readFromResource(String resourceFile) {
		try {
			FileObject resource = filer.getResource(resourcesPath, "", resourceFile);
			log("Looking for existing resource file at " + resource.toUri());
			try (BufferedReader reader = bufferedReader(resource)) {
				return reader.lines().map(String::trim).collect(Collectors.toUnmodifiableSet());
			}
		}
		catch (Exception ex) {
			log("Warning: Unable to read " + resourceFile + ", " + ex);
			return Collections.emptySet();
		}
	}

	/**
	 * 将配置信息写入到文件
	 * @param services 实现类信息
	 */
	private void writeFile(Collection<String> services, String resourceFile) {
		try {
			FileObject fileObject = filer.createResource(resourcesPath, "", resourceFile);
			try (BufferedWriter writer = bufferedWriter(fileObject)) {
				for (String service : services) {
					writer.write(service);
					writer.newLine();
				}
				writer.flush();
			}
		}
		catch (IOException ex) {
			fatalError("Unable to create " + resourceFile + ", " + ex);
		}
	}

}
