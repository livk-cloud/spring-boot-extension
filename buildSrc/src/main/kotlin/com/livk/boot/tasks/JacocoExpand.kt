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

package com.livk.boot.tasks

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport

/**
 * @author livk
 */
abstract class JacocoExpand : Plugin<Project> {
	companion object {
		private const val TASK_NAME = "jacocoTestReport"
	}

	override fun apply(project: Project) {
		project.pluginManager.apply(JacocoPlugin::class.java)

		project.tasks.withType(Test::class.java) {
			it.finalizedBy(TASK_NAME)
		}

		project.tasks.named(TASK_NAME, JacocoReport::class.java){ jacocoReport->
			jacocoReport.dependsOn("test")

			jacocoReport.reports{
				it.xml.required.set(true)
				it.html.required.set(false)
			}
		}
	}
}
