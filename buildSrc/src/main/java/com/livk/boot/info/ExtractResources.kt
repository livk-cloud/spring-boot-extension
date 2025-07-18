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

package com.livk.boot.info

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * @author livk
 */
open class ExtractResources : DefaultTask() {


	private var destinationDirectory: DirectoryProperty = project.objects.directoryProperty()

	private var resourceNames: List<String> = ArrayList()

	@Input
	fun getResourceNames(): List<String> = this.resourceNames

	fun setResourcesNames(resourceNames: List<String>) {
		this.resourceNames = resourceNames
	}

	@OutputDirectory
	fun getDestinationDirectory(): DirectoryProperty = this.destinationDirectory

	@TaskAction
	fun extractResources() {
		for (resourceName in this.resourceNames) {
			javaClass.classLoader.resources(resourceName).forEach { url ->
				if (url.path.contains("buildSrc")) {
					url.openStream().use {
						copy(it, FileOutputStream(destinationDirectory.file(resourceName).get().asFile))
					}
				}
			}
		}
	}

	fun copy(input: InputStream, out: OutputStream): Int {
		input.use {
			out.use {
				val count = input.transferTo(out).toInt()
				out.flush()
				return count
			}
		}
	}
}
