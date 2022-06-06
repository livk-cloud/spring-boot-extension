package com.livk.plugin


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * <p>
 * 给非SpringBoot项目添加Resources文件夹
 * </p>
 * <p>
 *     添加编译检查参数
 * </p>
 * <p>
 *     给含有mapstruct项目添加IGNORE属性
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
