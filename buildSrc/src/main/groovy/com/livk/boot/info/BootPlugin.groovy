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
abstract class BootPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.pluginManager.apply(JavaPlugin)
		project.pluginManager.apply(SpringBootPlugin)

		project.tasks.named(SpringBootPlugin.BOOT_JAR_TASK_NAME, BootJar) { bootJar ->
			bootJar.archiveBaseName.set(project.name)
			bootJar.archiveFileName.set("${bootJar.archiveBaseName.get()}.${bootJar.archiveExtension.get()}")
			bootJar.launchScript()
			bootJar.duplicatesStrategy = DuplicatesStrategy.EXCLUDE
		}

		project.tasks.named(JavaPlugin.JAR_TASK_NAME, Jar) { jar ->
			jar.enabled = false
		}
	}
}
