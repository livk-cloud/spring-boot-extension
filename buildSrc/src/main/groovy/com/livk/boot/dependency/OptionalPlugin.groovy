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
import org.gradle.api.plugins.JavaPluginExtension

/**
 * <p>
 * OptionalPlugin
 * </p>
 *
 * @author livk
 *
 */
abstract class OptionalPlugin implements Plugin<Project> {

	public static final String OPTIONAL = "optional"

	@Override
	void apply(Project project) {
		def configurations = project.configurations
		def optional = configurations.create(OPTIONAL)
		optional.canBeConsumed = false
		optional.canBeResolved = false
		project.getPlugins().withType(JavaPlugin.class).every {
			project.extensions.getByType(JavaPluginExtension.class).sourceSets.every { sourceSet ->
				configurations.named(sourceSet.getCompileClasspathConfigurationName()).get().extendsFrom(optional)
				configurations.named(sourceSet.getRuntimeClasspathConfigurationName()).get().extendsFrom(optional)
			}
		}
	}
}
