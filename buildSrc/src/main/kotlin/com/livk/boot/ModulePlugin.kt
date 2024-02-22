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

package com.livk.boot

import com.livk.boot.compile.CompileArgsPlugin
import com.livk.boot.info.ExtractResources
import com.livk.boot.tasks.JacocoExpand
import io.spring.javaformat.gradle.SpringJavaFormatPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar

/**
 * @author livk
 */
class ModulePlugin : Plugin<Project> {
	override fun apply(project: Project) {
		project.pluginManager.apply(CompileArgsPlugin::class.java)
		project.pluginManager.apply(CorePlugin::class.java)
		project.pluginManager.apply(SpringJavaFormatPlugin::class.java)
		project.pluginManager.apply(JacocoExpand::class.java)

		val extractLegalResources = project.tasks.create("extractLegalResources", ExtractResources::class.java)
		extractLegalResources.getDestinationDirectory().set(project.layout.buildDirectory.dir("legal"))
		extractLegalResources.setResourcesNames(listOf("LICENSE.txt"))

		project.tasks.withType(Jar::class.java) { jar ->
			project.afterEvaluate {
				jar.metaInf { metaInf ->
					metaInf.from(extractLegalResources)
				}
			}
		}
	}
}
