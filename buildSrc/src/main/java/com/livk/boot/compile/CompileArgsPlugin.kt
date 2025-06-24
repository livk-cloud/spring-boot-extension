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

package com.livk.boot.compile

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.api.tasks.testing.Test
import org.gradle.external.javadoc.JavadocOutputLevel
import org.gradle.external.javadoc.StandardJavadocDocletOptions

/**
 * @author livk
 */
abstract class CompileArgsPlugin : Plugin<Project> {
	companion object {
		private val COMPILER_ARGS = arrayListOf<String>()
		private const val MAPSTRUCT_PROCESSOR_NAME = "mapstruct-processor"
		private val MAPSTRUCT_COMPILER_ARGS = arrayListOf<String>()
		private const val UTF_8 = "UTF-8"
	}

	init {
		COMPILER_ARGS.addAll(
			listOf(
				"-Xlint:-options",
				"-Xlint:varargs",
				"-Xlint:rawtypes",
				"-Xlint:deprecation",
				"-Xlint:unchecked",
				"-Werror",
				"-parameters"
			)
		)
		MAPSTRUCT_COMPILER_ARGS.addAll(listOf("-Amapstruct.unmappedTargetPolicy=IGNORE"))
	}

	override fun apply(project: Project) {
		project.pluginManager.apply(JavaPlugin::class.java)
		project.tasks.withType(Javadoc::class.java) { javadoc ->
			val options = javadoc.options as StandardJavadocDocletOptions
			options.encoding(UTF_8)
			javadoc.isFailOnError = false
			options.outputLevel = JavadocOutputLevel.QUIET
			options.addStringOption("Xdoclint:none", "-quiet")
		}

		project.tasks.withType(JavaCompile::class.java).matching { compileTask ->
			compileTask.name == JavaPlugin.COMPILE_JAVA_TASK_NAME ||
				compileTask.name == JavaPlugin.COMPILE_TEST_JAVA_TASK_NAME ||
				compileTask.name == "compileTestFixturesJava"
		}.forEach(::addCompile)

		project.tasks.withType(Test::class.java, Test::useJUnitPlatform)

		project.afterEvaluate {
			val dependencyName = hashSetOf<String>()
			project.configurations.forEach {
				dependencyName.addAll(it.dependencies.map { dependency -> dependency.name })
			}
			if (dependencyName.contains(MAPSTRUCT_PROCESSOR_NAME)) {
				project.tasks.named(JavaPlugin.COMPILE_JAVA_TASK_NAME, JavaCompile::class.java) {
					it.options.compilerArgs.addAll(MAPSTRUCT_COMPILER_ARGS)
				}
			}
		}
	}

	private fun addCompile(javaCompile: JavaCompile) {
		javaCompile.options.compilerArgs.addAll(COMPILER_ARGS)
		javaCompile.options.encoding = UTF_8
		javaCompile.sourceCompatibility = JavaVersion.VERSION_21.toString()
		javaCompile.targetCompatibility = JavaVersion.VERSION_21.toString()
	}
}
