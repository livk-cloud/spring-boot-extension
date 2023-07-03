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

package com.livk.boot.compile

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin

/**
 * @author livk
 */
abstract class ResourcesPlugin : Plugin<Project> {

	override fun apply(project: Project) {
		project.pluginManager.apply(JavaLibraryPlugin::class.java)
		project.tasks
			.named(JavaPlugin.COMPILE_JAVA_TASK_NAME).get()
			.dependsOn(JavaPlugin.PROCESS_RESOURCES_TASK_NAME)
	}
}
