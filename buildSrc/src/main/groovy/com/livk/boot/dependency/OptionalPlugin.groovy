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

/**
 * @author livk
 */
abstract class OptionalPlugin implements Plugin<Project> {

	static final String OPTIONAL = 'optional'

	@Override
	void apply(Project project) {
		def configurations = project.configurations
		project.pluginManager.apply(JavaPlugin)
		configurations.create(OPTIONAL) { Configuration optional ->
			optional.canBeResolved = false
			optional.canBeConsumed = false
			project.plugins.withType(JavaPlugin).configureEach {
				project.extensions.getByType(JavaPluginExtension).with {
					def optionalSourceSet = sourceSets.create(OPTIONAL)
					registerFeature(OPTIONAL) { feature ->
						feature.usingSourceSet(optionalSourceSet)
					}
					sourceSets.configureEach { sourceSet ->
						configurations.named(sourceSet.compileClasspathConfigurationName).get().extendsFrom(optional)
						configurations.named(sourceSet.runtimeClasspathConfigurationName).get().extendsFrom(optional)
						configurations.named("${OPTIONAL}${JavaPlugin.API_CONFIGURATION_NAME.capitalize()}") { config ->
							config.extendsFrom(optional)
						}
					}
					sourceSets.remove(optionalSourceSet)
				}
			}

			project.tasks.withType(Jar).configureEach { jar ->
				if (jar.name == "${OPTIONAL}${JavaPlugin.JAR_TASK_NAME.capitalize()}") {
					jar.enabled = false
				}
			}
		}
	}
}
