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
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

/**
 * @author livk
 */
abstract class MavenRepositoryPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		project.pluginManager.apply(MavenPublishPlugin::class.java)
		project.extensions.getByType(PublishingExtension::class.java).apply {
			repositories.mavenLocal()
			try {
				val releasesRepoUrl = project.property("mvn.releasesRepoUrl").toString()
				val snapshotsRepoUrl = project.property("mvn.releasesRepoUrl").toString()
				repositories.maven { maven ->
					//使用不安全的http请求、也就是缺失SSL
					maven.name = "CustomizeMaven"
					maven.isAllowInsecureProtocol = true
					val url = if (project.version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
					maven.setUrl(url)
					maven.credentials {
						it.username = project.property("mvn.username").toString()
						it.password = project.property("mvn.password").toString()
					}
				}
			} catch (_: Exception) {

			}
		}
	}

}
