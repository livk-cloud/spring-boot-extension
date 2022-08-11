package com.livk.boot

import com.livk.boot.maven.DeployedPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlatformExtension
import org.gradle.api.plugins.JavaPlatformPlugin

/**
 * <p>
 * BomPlugin
 * </p>
 *
 * @author livk
 * @date 2022/8/11
 */
class BomPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply(JavaPlatformPlugin.class)
        project.pluginManager.apply(DeployedPlugin.class)

        project.extensions.getByType(JavaPlatformExtension.class).allowDependencies()
    }
}
