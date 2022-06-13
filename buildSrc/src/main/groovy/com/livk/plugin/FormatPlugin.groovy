package com.livk.plugin

import io.spring.javaformat.gradle.SpringJavaFormatPlugin
import io.spring.javaformat.gradle.tasks.Format
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * <p>
 * spring format格式化
 * </p>
 *
 * @author livk
 * @date 2022/6/6
 */
abstract class FormatPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply(JavaPlugin.class)
        project.pluginManager.apply(SpringJavaFormatPlugin.class)
        project.tasks
                .getByName(JavaPlugin.COMPILE_JAVA_TASK_NAME)
                .dependsOn(Format.NAME)
    }
}
