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

package com.livk.boot.tasks

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport

/**
 * @author livk
 */
abstract class JacocoExpandPlugin implements Plugin<Project> {

	static final String TASK_NAME = 'jacocoTestReport'

	@Override
	void apply(Project project) {
		project.pluginManager.apply(JacocoPlugin)

		project.tasks.withType(Test).configureEach { test ->
			test.finalizedBy(TASK_NAME)
		}

		project.tasks.named(TASK_NAME, JacocoReport) { jacocoReport ->
			jacocoReport.dependsOn('test')
			jacocoReport.reports { reports ->
				reports.xml.required.set(true)
				reports.html.required.set(false)
			}
		}
	}
}
