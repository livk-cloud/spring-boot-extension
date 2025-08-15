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

import com.google.auto.common.MoreTypes;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author livk
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
abstract class AbstractFactoriesProcessor extends CustomizeAbstractProcessor {

	private final String location;

	@Override
	protected void generateConfigFiles() {
		if (!processorMap.isEmpty()) {
			Multimap<String, String> allImportMap = this.readFromResource();
			if (!allImportMap.isEmpty()) {
				log("Existing service entries: " + allImportMap);
			}
			for (Map.Entry<String, String> entry : processorMap.entries()) {
				allImportMap.put(entry.getKey(), entry.getValue());
			}
			this.writeFile(allImportMap.asMap());
		}
	}

	@Override
	protected Set<TypeElement> elseElement(Element element) {
		if (element instanceof TypeElement typeElement) {
			List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
			if (interfaces != null && interfaces.size() == 1) {
				return Set.of(MoreTypes.asTypeElement(interfaces.getFirst()));
			}
		}
		return Set.of();
	}

	/**
	 * 从文件读取某个接口的配置
	 * @return set className
	 */
	private Multimap<String, String> readFromResource() {
		try {
			FileObject resource = filer.getResource(resourcesPath, "", location);
			log("Looking for existing resource file at " + resource.toUri());
			try (BufferedReader reader = bufferedReader(resource)) {
				Properties properties = new Properties();
				properties.load(reader);
				Multimap<String, String> providers = LinkedHashMultimap.create();
				for (Map.Entry<Object, Object> entry : properties.entrySet()) {
					String factoryTypeName = ((String) entry.getKey()).trim();
					String[] factoryImplementationNames = ((String) entry.getValue()).split(",");
					providers.putAll(factoryTypeName, Arrays.asList(factoryImplementationNames));
				}
				return providers;
			}
		}
		catch (Exception ex) {
			log("Warning: Unable to read " + location + ", " + ex);
			return LinkedHashMultimap.create();
		}
	}

	/**
	 * 将配置信息写入到文件
	 * @param allImportMap 供应商接口及实现类信息
	 */
	private void writeFile(Map<String, ? extends Collection<String>> allImportMap) {
		try {
			FileObject fileObject = filer.createResource(resourcesPath, "", location);
			try (BufferedWriter writer = bufferedWriter(fileObject)) {
				for (Map.Entry<String, ? extends Collection<String>> entry : allImportMap.entrySet()) {
					String providerInterface = entry.getKey();
					Collection<String> services = entry.getValue();
					writer.write(providerInterface);
					writer.write("=\\");
					writer.newLine();
					String[] serviceArrays = services.toArray(String[]::new);
					for (int i = 0; i < serviceArrays.length; i++) {
						writer.write(serviceArrays[i]);
						if (i != serviceArrays.length - 1) {
							writer.write(",\\");
						}
						writer.newLine();
					}
					writer.newLine();
				}
				writer.newLine();
				writer.flush();
			}
		}
		catch (IOException ex) {
			fatalError("Unable to create " + location + ", " + ex);
		}
	}

}
