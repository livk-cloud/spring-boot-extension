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
import org.gradle.api.plugins.JavaTestFixturesPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

/**
 * @author livk
 */
abstract class ManagementPlugin implements Plugin<Project> {

	static final String MANAGEMENT = 'management'

	@Override
	void apply(Project project) {
		def configurations = project.configurations
		project.pluginManager.apply(JavaPlugin)
		configurations.create(MANAGEMENT) { Configuration management ->
			management.canBeResolved = false
			management.canBeConsumed = false
			def plugins = project.plugins
			plugins.withType(JavaPlugin).configureEach {
				project.extensions.getByType(JavaPluginExtension).sourceSets.configureEach { sourceSet ->
					configurations.named(sourceSet.compileClasspathConfigurationName).get().extendsFrom(management)
					configurations.named(sourceSet.runtimeClasspathConfigurationName).get().extendsFrom(management)
					configurations.named(sourceSet.annotationProcessorConfigurationName).get().extendsFrom(management)
				}
			}
			plugins.withType(JavaTestFixturesPlugin).configureEach {
				configurations.named('testFixturesCompileClasspath').get().extendsFrom(management)
				configurations.named('testFixturesRuntimeClasspath').get().extendsFrom(management)
			}
			plugins.withType(MavenPublishPlugin).configureEach {
				project.extensions.getByType(PublishingExtension).publications
					.withType(MavenPublication).configureEach { mavenPublication ->
					mavenPublication.versionMapping { versions ->
						versions.allVariants { it.fromResolutionResult() }
					}
				}
			}
		}
	}
}
