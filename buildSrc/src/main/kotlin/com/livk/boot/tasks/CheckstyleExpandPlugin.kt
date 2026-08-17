/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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

package com.livk.boot.tasks

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.plugins.quality.CheckstylePlugin
import java.io.File

/**
 * @author livk
 * @date 2026/8/17
 */
abstract class CheckstyleExpandPlugin: Plugin<Project> {

	override fun apply(project: Project) {
		project.pluginManager.apply(CheckstylePlugin::class.java)

		project.extensions.getByType(CheckstyleExtension::class.java).run {
			val checkstyleVersion = project.rootProject
				.extensions
				.getByType(VersionCatalogsExtension::class.java)
				.named("libs")
				.findVersion("checkstyle")
				.get()
				.displayName
			toolVersion = checkstyleVersion
			configFile = File("${project.rootDir.path}/src/checkstyle/checkstyle.xml")
		}

		project.tasks.withType(Checkstyle::class.java).matching { it.name == "checkstyleTest" }.configureEach {
			enabled = false
		}

		project.tasks.register("checkstyle") {
			group = "other"
			dependsOn("checkstyleMain")
			dependsOn(project.provider {
				project.tasks.findByName("checkFormat")?.let { listOf(it) } ?: emptyList()
			})
		}
	}
}
