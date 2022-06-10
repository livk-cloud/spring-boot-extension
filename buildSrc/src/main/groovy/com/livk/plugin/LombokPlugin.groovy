package com.livk.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * <p>
 * Lombok引入插件
 * </p>
 *
 * @author livk
 * @date 2022/6/10
 */
abstract class LombokPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.apply(JavaPlugin.class)
        def lombok = project.dependencies.create("org.projectlombok:lombok")
        project.dependencies.add(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME, lombok)
        project.dependencies.add(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME, lombok)
        project.dependencies.add(JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME, lombok)
    }
}
