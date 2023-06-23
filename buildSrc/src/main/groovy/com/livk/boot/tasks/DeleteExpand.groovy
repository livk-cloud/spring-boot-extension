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

package com.livk.boot.tasks

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.language.base.plugins.LifecycleBasePlugin

/**
 * <p>
 * DeleteExpand
 * </p>
 *
 * @author livk
 *
 */
abstract class DeleteExpand implements Plugin<Project> {

	public static final String CLEAN_ALL_TASK_NAME = "cleanAll"

	@Override
	void apply(Project project) {
		project.tasks.withType(Delete.class).every {
			it.delete(project.projectDir.absolutePath + "/build")
			it.delete(project.projectDir.absolutePath + "/out")
			it.delete(project.projectDir.absolutePath + "/bin")
		}

		project.tasks.register(CLEAN_ALL_TASK_NAME, Delete.class) {
			setGroup(LifecycleBasePlugin.BUILD_GROUP)
			it.delete(project.projectDir.absolutePath + "/src/main/generated")
			it.delete(project.projectDir.absolutePath + "/src/test/generated_tests")
			dependsOn(LifecycleBasePlugin.CLEAN_TASK_NAME)
		}

	}
}
