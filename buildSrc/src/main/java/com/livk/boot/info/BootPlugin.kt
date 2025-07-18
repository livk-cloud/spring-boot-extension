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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.bundling.Jar
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar

/**
 * @author livk
 */
abstract class BootPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		project.pluginManager.apply(JavaPlugin::class.java)
		project.pluginManager.apply(SpringBootPlugin::class.java)

		project.tasks.named(SpringBootPlugin.BOOT_JAR_TASK_NAME, BootJar::class.java) {
			it.archiveBaseName.set(project.name)
			it.archiveFileName.set("${it.archiveBaseName.get()}.${it.archiveExtension.get()}")
			it.launchScript()
			it.duplicatesStrategy = DuplicatesStrategy.EXCLUDE
		}

		project.tasks.named(JavaPlugin.JAR_TASK_NAME, Jar::class.java) {
			it.enabled = false
		}
	}
}
