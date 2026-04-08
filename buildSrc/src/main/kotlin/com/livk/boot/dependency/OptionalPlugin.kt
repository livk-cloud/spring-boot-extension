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
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.bundling.Jar
import org.gradle.internal.impldep.org.bouncycastle.oer.OERDefinition.optional
import sun.tools.jar.resources.jar

/**
 * @author livk
 */
abstract class OptionalPlugin : Plugin<Project> {

	companion object {
		const val OPTIONAL = "optional"
	}

	override fun apply(project: Project) {
		project.pluginManager.apply(JavaPlugin::class.java)
		val configurations = project.configurations
		val optional = configurations.create(OPTIONAL).apply {
			isCanBeResolved = false
			isCanBeConsumed = false
		}
		project.plugins.withType(JavaPlugin::class.java).configureEach {
			val javaExt = project.extensions.getByType(JavaPluginExtension::class.java)
			with(javaExt) {
				val optionalSourceSet = sourceSets.create(OPTIONAL)
				registerFeature(OPTIONAL) {
					usingSourceSet(optionalSourceSet)
				}
				sourceSets.configureEach {
					configurations.named(compileClasspathConfigurationName).configure {
						extendsFrom(optional)
					}
					configurations.named(runtimeClasspathConfigurationName).configure {
						extendsFrom(optional)
					}
					configurations.named("${OPTIONAL}${JavaPlugin.API_CONFIGURATION_NAME.replaceFirstChar { c -> c.uppercase() }}")
						.configure {
							extendsFrom(optional)
						}
				}
				sourceSets.remove(optionalSourceSet)
			}
		}
		project.tasks.withType(Jar::class.java) {
			if (name == "${OPTIONAL}${JavaPlugin.JAR_TASK_NAME.replaceFirstChar { it.uppercase() }}") {
				isEnabled = false
			}
		}
	}
}
