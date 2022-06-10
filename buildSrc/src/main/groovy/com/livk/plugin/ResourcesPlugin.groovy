package com.livk.plugin


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * <p>
 *     资源copy
 * </p>
 *
 * @author livk
 * @date 2022/4/24
 */
abstract class ResourcesPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.tasks
                .getByName(JavaPlugin.COMPILE_JAVA_TASK_NAME)
                .dependsOn(JavaPlugin.PROCESS_RESOURCES_TASK_NAME)
    }
}
