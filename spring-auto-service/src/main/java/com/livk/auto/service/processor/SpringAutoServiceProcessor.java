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

package com.livk.auto.service.processor;

import com.google.auto.service.AutoService;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.livk.auto.service.annotation.SpringAutoService;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * SpringAutoServiceProcessor
 * </p>
 *
 * @author livk
 */
@AutoService(Processor.class)
public class SpringAutoServiceProcessor extends CustomizeAbstractProcessor {

	private static final Class<SpringAutoService> SUPPORT_CLASS = SpringAutoService.class;

	private static final String AUTOCONFIGURATION = "org.springframework.boot.autoconfigure.AutoConfiguration";

	private static final String LOCATION = "META-INF/spring/%s.imports";

	private final SetMultimap<String, String> importsMap = Multimaps
		.synchronizedSetMultimap(LinkedHashMultimap.create());

	@Override
	protected Set<Class<?>> getSupportedAnnotation() {
		return Set.of(SUPPORT_CLASS);
	}

	@Override
	protected void generateConfigFiles() {
		for (String providerInterface : importsMap.keySet()) {
			String resourceFile = String.format(LOCATION, providerInterface);
			log("Working on resource file: " + resourceFile);
			try {
				FileObject resource = filer.getResource(out, "", resourceFile);
				log("Looking for existing resource file at " + resource.toUri());
				Set<String> exitImports = this.read(resource);
				log("Existing service entries: " + exitImports);
				Set<String> allImports = Stream.concat(exitImports.stream(), importsMap.get(providerInterface).stream())
					.collect(Collectors.toSet());

				FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);

				this.writeFile(allImports, fileObject);
			}
			catch (IOException e) {
				fatalError("Unable to create " + resourceFile + ", " + e);
				return;
			}
		}
	}

	@Override
	protected void processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(SUPPORT_CLASS);
		log(annotations.toString());
		log(elements.toString());
		for (Element element : elements) {
			Optional<Set<TypeElement>> value = TypeElements.getAnnotationAttributes(element, SUPPORT_CLASS, "value");
			for (TypeElement typeElement : value.orElse(autoConfigurationElement())) {
				String provider = TypeElements.getBinaryName(typeElement);
				importsMap.put(provider, TypeElements.getBinaryName((TypeElement) element));
			}
		}
	}

	private Set<TypeElement> autoConfigurationElement() {
		return Set.of(elements.getTypeElement(AUTOCONFIGURATION));
	}

	/**
	 * 从文件读取配置
	 * @param fileObject 文件信息
	 * @return set className
	 */
	private Set<String> read(FileObject fileObject) {
		try (BufferedReader reader = bufferedReader(fileObject)) {
			return reader.lines().map(String::trim).collect(Collectors.toUnmodifiableSet());
		}
		catch (Exception ignored) {
			return Collections.emptySet();
		}
	}

	/**
	 * 将配置信息写入到文件
	 * @param services 实现类信息
	 * @param fileObject 文件信息
	 */
	private void writeFile(Collection<String> services, FileObject fileObject) throws IOException {
		try (BufferedWriter writer = bufferedWriter(fileObject)) {
			for (String service : services) {
				writer.write(service);
				writer.newLine();
			}
			writer.flush();
		}
	}

}
