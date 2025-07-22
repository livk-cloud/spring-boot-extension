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

package com.livk.boot.dependency

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * @author livk
 */
abstract class AptCompilePlugin : Plugin<Project> {

	companion object {
		const val APT_COMPILE = "aptCompile"

		val DEPENDENCY_NAMES_SET = HashSet<String>()

		init {
			DEPENDENCY_NAMES_SET.addAll(
				setOf(
					JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME,
					JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME,
					JavaPlugin.TEST_COMPILE_CLASSPATH_CONFIGURATION_NAME,
					JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME
				)
			)
		}
	}

	override fun apply(project: Project) {
		val configurations = project.configurations
		project.pluginManager.apply(JavaPlugin::class.java)
		configurations.create(APT_COMPILE) { apt ->
			apt.isVisible = false
			apt.isCanBeResolved = false
			apt.isCanBeConsumed = false
			project.plugins.withType(JavaPlugin::class.java) {
				DEPENDENCY_NAMES_SET.forEach { configurations.getByName(it).extendsFrom(apt) }
			}
		}
	}
}
