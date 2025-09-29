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

package com.livk.boot.info

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * @author livk
 */
class ExtractResources extends DefaultTask {

	private final DirectoryProperty destinationDirectory = project.objects.directoryProperty()
	private List<String> resourceNames = []

	@Input
	List<String> getResourceNames() {
		return resourceNames
	}

	void setResourceNames(List<String> resourceNames) {
		this.resourceNames = resourceNames
	}

	@OutputDirectory
	DirectoryProperty getDestinationDirectory() {
		return destinationDirectory
	}

	@TaskAction
	void extractResources() {
		resourceNames.each { resourceName ->
			getClass().classLoader.getResources(resourceName).each { url ->
				if (url.path.contains('buildSrc')) {
					url.openStream().withCloseable { input ->
						def outputFile = destinationDirectory.file(resourceName).get().asFile
						new FileOutputStream(outputFile).withCloseable { output ->
							copy(input, output)
						}
					}
				}
			}
		}
	}

	static int copy(InputStream input, OutputStream output) {
		input.withCloseable {
			output.withCloseable {
				def count = input.transferTo(output) as int
				output.flush()
				return count
			}
		}
	}
}
