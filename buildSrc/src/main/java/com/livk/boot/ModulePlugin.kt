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

package com.livk.boot

import com.livk.boot.compile.CompileArgsPlugin
import com.livk.boot.info.ExtractResources
import io.spring.javaformat.gradle.SpringJavaFormatPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.plugins.quality.CheckstylePlugin
import org.gradle.api.tasks.bundling.Jar
import java.io.File

/**
 * @author livk
 */
class ModulePlugin : Plugin<Project> {
	override fun apply(project: Project) {
		project.pluginManager.apply(CompileArgsPlugin::class.java)
		project.pluginManager.apply(CorePlugin::class.java)
		project.pluginManager.apply(SpringJavaFormatPlugin::class.java)

		project.pluginManager.apply(CheckstylePlugin::class.java)

		project.extensions.getByType(CheckstyleExtension::class.java).apply {
			toolVersion = "9.3"
			configFile = File("${project.rootDir.path}/src/checkstyle/checkstyle.xml")
		}

		val version = project.rootProject
			.extensions
			.getByType(VersionCatalogsExtension::class.java)
			.named("libs")
			.findVersion("spring-javaformat")
			.get()
			.displayName

		project.dependencies.add("checkstyle", "io.spring.javaformat:spring-javaformat-checkstyle:${version}")

		project.tasks.register("checkstyle") { checkstyle ->
			checkstyle.group = "other"
			checkstyle.dependsOn("checkstyleMain", "checkstyleTest", "checkFormat")
		}

		val extractResourcesProvider =
			project.tasks.register("extractLegalResources", ExtractResources::class.java) {
				it.getDestinationDirectory().set(project.layout.buildDirectory.dir("legal"))
				it.setResourcesNames(listOf("LICENSE.txt"))
			}

		project.tasks.withType(Jar::class.java) { jar ->
			jar.dependsOn(extractResourcesProvider)
			jar.metaInf { metaInf ->
				metaInf.from(extractResourcesProvider)
			}
		}
	}
}
