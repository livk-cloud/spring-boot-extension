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

package com.livk.boot.maven

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.plugins.signing.SigningPlugin

/**
 * @author livk
 */
abstract class DeployedPlugin : Plugin<Project> {

	override fun apply(project: Project) {
		project.pluginManager.apply(SigningPlugin::class.java)
		project.pluginManager.apply(MavenRepositoryPlugin::class.java)
		project.pluginManager.apply(MavenPortalPublishPlugin::class.java)
	}
}
