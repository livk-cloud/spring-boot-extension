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

/**
 * @author livk
 */
abstract class AptCompilePlugin implements Plugin<Project> {

	static final String APT_COMPILE = 'aptCompile'

	static final Set<String> DEPENDENCY_NAMES_SET = [
		JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME,
		JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME,
		JavaPlugin.TEST_COMPILE_CLASSPATH_CONFIGURATION_NAME,
		JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME
	] as HashSet

	@Override
	void apply(Project project) {
		project.pluginManager.apply(JavaPlugin)
		def configurations = project.configurations
		configurations.create(APT_COMPILE) { Configuration apt ->
			apt.canBeResolved = false
			apt.canBeConsumed = false
			project.plugins.withType(JavaPlugin).configureEach {
				DEPENDENCY_NAMES_SET.each { configName ->
					configurations.named(configName).get().extendsFrom(apt)
				}
			}
		}
	}
}
