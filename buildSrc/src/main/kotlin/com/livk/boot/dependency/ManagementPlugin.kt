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

package com.livk.boot.dependency

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaTestFixturesPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

/**
 * @author livk
 */
abstract class ManagementPlugin : Plugin<Project> {
	companion object {
		const val MANAGEMENT = "management"

		val DEPENDENCY_NAMES_SET = hashSetOf<String>()

		init {
			DEPENDENCY_NAMES_SET.addAll(
				setOf(
					JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME,
					JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME,
					JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME,
					JavaPlugin.TEST_COMPILE_CLASSPATH_CONFIGURATION_NAME,
					JavaPlugin.TEST_RUNTIME_CLASSPATH_CONFIGURATION_NAME,
					JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME
				)
			)
		}
	}

	override fun apply(project: Project) {
		val configurations = project.configurations
		project.pluginManager.apply(JavaPlugin::class.java)
		configurations.create(MANAGEMENT) { management ->
			management.isVisible = false
			management.isCanBeResolved = false
			management.isCanBeConsumed = false
			val plugins = project.plugins
			plugins.withType(JavaPlugin::class.java) {
				DEPENDENCY_NAMES_SET.forEach { configurations.getByName(it).extendsFrom(management) }
			}
			plugins.withType(JavaTestFixturesPlugin::class.java) {
				configurations.getByName("testFixturesCompileClasspath").extendsFrom(management)
				configurations.getByName("testFixturesRuntimeClasspath").extendsFrom(management)
			}
			plugins.withType(MavenPublishPlugin::class.java) {
				project.extensions.getByType(PublishingExtension::class.java).publications
					.withType(MavenPublication::class.java) { mavenPublication ->
						mavenPublication.versionMapping { versions ->
							versions.allVariants {
								it.fromResolutionResult()
							}
						}
					}
			}
		}
	}
}
