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
import org.gradle.api.plugins.JavaPlatformPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.plugins.signing.SigningExtension
import org.gradle.plugins.signing.SigningPlugin

/**
 * @author livk
 */
abstract class DeployedPlugin : Plugin<Project> {
	companion object {
		const val NAME = "maven"
	}

	override fun apply(project: Project) {
		project.pluginManager.apply(SigningPlugin::class.java)
		val publication = publication(project)
		val signing = project.extensions.getByType(SigningExtension::class.java)
		signing.isRequired = false
		signing.sign(publication)
		project.afterEvaluate {
			project.plugins.withType(JavaPlugin::class.java) {
				if ((project.tasks.named(JavaPlugin.JAR_TASK_NAME).get() as Jar).isEnabled) {
					val javaPluginExtension = project.extensions.getByType(JavaPluginExtension::class.java)
					javaPluginExtension.withSourcesJar()
					javaPluginExtension.withJavadocJar()
					project.components
						.matching { softwareComponent -> softwareComponent.name == "java" }
						.all { publication.from(it) }
					mavenInfo(publication, project)
				}
			}
		}
		project.plugins.withType(JavaPlatformPlugin::class.java) {
			project.components
				.matching { softwareComponent -> softwareComponent.name == "javaPlatform" }
				.all { publication.from(it) }
			mavenInfo(publication, project)
		}
	}

	private fun publication(project: Project): MavenPublication {
		project.pluginManager.apply(MavenPublishPlugin::class.java)
		project.pluginManager.apply(MavenRepositoryPlugin::class.java)
		return project.extensions
			.getByType(PublishingExtension::class.java)
			.publications
			.create(NAME, MavenPublication::class.java)

	}

	private fun mavenInfo(publication: MavenPublication, project: Project) {
		publication.versionMapping { versionMappingStrategy ->
			versionMappingStrategy.allVariants { variantVersionMappingStrategy ->
				variantVersionMappingStrategy.fromResolutionResult()
			}
		}
		publication.pom { pom ->
			pom.name.set(project.name)
			pom.description.set(description(project))
			pom.url.set("https://github.com/livk-cloud/" + project.rootProject.name)
			pom.licenses { licenses ->
				licenses.license { license ->
					license.name.set("The Apache License, Version 2.0")
					license.url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
				}
			}
			pom.developers { developers ->
				developers.developer { developer ->
					developer.name.set("livk")
					developer.email.set("livk.cloud@gmail.com")
				}
			}
			pom.scm { scm ->
				scm.connection.set("git@github.com:livk-cloud/" + project.rootProject.name + ".git")
				scm.url.set("https://github.com/livk-cloud/" + project.rootProject.name + "/")
			}
		}
	}

	private fun description(project: Project): String {
		return if (project.description.isNullOrBlank())
			project.name.replace("-", " ")
		else
			project.description!!
	}
}
