package com.livk.kt.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * <p>
 * ResourcesPlugin
 * </p>
 *
 * @author livk
 * @date 2022/4/24
 */
class ResourcesPlugin:Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks
            .getByName(JavaPlugin.COMPILE_JAVA_TASK_NAME)
            .dependsOn(JavaPlugin.PROCESS_RESOURCES_TASK_NAME)
    }
}
