package com.livk.boot

import com.livk.boot.compile.ResourcesPlugin
import com.livk.boot.maven.DeployedPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin

/**
 * <p>
 * CommonPlugin
 * </p>
 *
 * @author livk
 *
 */
class CommonPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply(JavaLibraryPlugin.class)
        project.pluginManager.apply(ModulePlugin.class)
        project.pluginManager.apply(ResourcesPlugin.class)
        project.pluginManager.apply(DeployedPlugin.class)
    }
}
