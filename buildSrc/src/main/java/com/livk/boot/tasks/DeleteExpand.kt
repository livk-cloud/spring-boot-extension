/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.boot.tasks

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete

/**
 * @author livk
 */
abstract class DeleteExpand : Plugin<Project> {

	companion object {
		private val cleanFiles = setOf("build", "out", "bin", "src/main/generated", "src/test/generated_tests")
	}

	override fun apply(project: Project) {
		project.tasks.withType(Delete::class.java) {
			cleanFiles.forEach { fileName ->
				it.delete("${project.projectDir.absolutePath}/$fileName")
			}
		}
	}
}
