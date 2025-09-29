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
abstract class CompileArgsPlugin implements Plugin<Project> {

	static final List<String> COMPILER_ARGS = [
		'-Xlint:-options',
		'-Xlint:varargs',
		'-Xlint:rawtypes',
		'-Xlint:deprecation',
		'-Xlint:unchecked',
		'-Werror',
		'-parameters'
	]

	static final String MAPSTRUCT_PROCESSOR_NAME = 'mapstruct-processor'

	static final List<String> MAPSTRUCT_COMPILER_ARGS = [
		'-Amapstruct.unmappedTargetPolicy=IGNORE'
	]

	static final String UTF_8 = 'UTF-8'

	@Override
	void apply(Project project) {
		project.pluginManager.apply(JavaPlugin.class)

		project.tasks.withType(Javadoc).configureEach { javadoc ->
			def options = javadoc.options as StandardJavadocDocletOptions
			options.encoding = UTF_8
			javadoc.failOnError = false
			options.outputLevel = JavadocOutputLevel.QUIET
			options.addStringOption('Xdoclint:none', '-quiet')
		}

		project.tasks.withType(JavaCompile).matching { compileTask ->
			compileTask.name in [
				JavaPlugin.COMPILE_JAVA_TASK_NAME,
				JavaPlugin.COMPILE_TEST_JAVA_TASK_NAME,
				'compileTestFixturesJava'
			]
		}.configureEach { javaCompile ->
			addCompile(javaCompile)
		}

		project.tasks.withType(Test).configureEach { test ->
			test.useJUnitPlatform()
		}

		project.afterEvaluate {
			def dependencyName = new HashSet()
			project.configurations.each { config ->
				dependencyName.addAll(config.dependencies.name)
			}
			if (dependencyName.contains(MAPSTRUCT_PROCESSOR_NAME)) {
				project.tasks.named(JavaPlugin.COMPILE_JAVA_TASK_NAME, JavaCompile) { task ->
					task.options.compilerArgs.addAll(MAPSTRUCT_COMPILER_ARGS)
				}
			}
		}
	}

	static void addCompile(JavaCompile javaCompile) {
		javaCompile.options.compilerArgs.addAll(COMPILER_ARGS)
		javaCompile.options.encoding = UTF_8
		javaCompile.sourceCompatibility = JavaVersion.VERSION_21.toString()
		javaCompile.targetCompatibility = JavaVersion.VERSION_21.toString()
	}
}
