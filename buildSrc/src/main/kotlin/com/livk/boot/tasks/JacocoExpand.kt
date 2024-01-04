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

		project.tasks.named(TASK_NAME, JacocoReport::class.java){
			it.dependsOn("test")

			it.reports{
				it.xml.required.set(true)
				it.html.required.set(false)
			}
		}
	}
}
