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

package com.livk.boot.test

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.kotlin.dsl.invoke
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.testing.base.TestingExtension

/**
 * <p>
 * IntegrationTestPlugin
 * </p>
 *
 * @author livk
 * @date 2026/5/28
 */
abstract class IntegrationTestPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		project.pluginManager.apply(JavaPlugin::class.java)
		val testing = project.extensions.getByType(TestingExtension::class.java)
		testing.suites.register("integrationTest", JvmTestSuite::class.java) {
			useJUnitJupiter()
			dependencies {
				implementation(project())
			}
			targets.all {
				testTask.configure {
					description = "Runs integration tests"
					group = LifecycleBasePlugin.VERIFICATION_GROUP
					shouldRunAfter(project.tasks.named("test"))
				}
			}
		}

		project.configurations.named("integrationTestImplementation") {
			extendsFrom(project.configurations.getByName("testImplementation"))
		}
		project.configurations.named ("integrationTestRuntimeOnly") {
			extendsFrom(
				project.configurations.getByName("testRuntimeOnly")
			)
		}
		project.configurations.named("integrationTestCompileClasspath"){
			extendsFrom(project.configurations.getByName(JavaPlugin.TEST_COMPILE_CLASSPATH_CONFIGURATION_NAME))
		}
		project.configurations.named("integrationTestAnnotationProcessor"){
			extendsFrom(project.configurations.getByName(JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME))
		}

//		project.tasks.named("check") {
//			dependsOn(testing.suites.named("integrationTest"))
//		}
	}
}
