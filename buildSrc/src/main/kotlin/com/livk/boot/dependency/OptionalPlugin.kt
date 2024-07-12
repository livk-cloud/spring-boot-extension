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

package com.livk.boot.dependency

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.configurationcache.extensions.capitalized

/**
 * @author livk
 */
abstract class OptionalPlugin : Plugin<Project> {
	companion object {
		const val OPTIONAL = "optional"
	}

	override fun apply(project: Project) {
		val configurations = project.configurations
		project.pluginManager.apply(JavaPlugin::class.java)
		configurations.create(OPTIONAL) { optional ->
			optional.isCanBeResolved = false
			optional.isCanBeConsumed = false
			project.plugins.withType(JavaPlugin::class.java) {
				val extension = project.extensions.getByType(JavaPluginExtension::class.java)
				extension.sourceSets.create(OPTIONAL)
				extension.registerFeature(OPTIONAL) {
					it.usingSourceSet(extension.sourceSets.getAt(OPTIONAL))
				}
				extension.sourceSets.all { sourceSet ->
					configurations.getByName(sourceSet.compileClasspathConfigurationName).extendsFrom(optional)
					configurations.getByName(sourceSet.runtimeClasspathConfigurationName).extendsFrom(optional)
					if (sourceSet.name != OPTIONAL) {
						configurations.getByName(OPTIONAL + "Api")
							.extendsFrom(optional)
					}
				}
			}
		}
	}
}
