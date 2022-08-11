package com.livk.boot

import com.livk.boot.info.BootPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * <p>
 * ServicePlugin
 * </p>
 *
 * @author livk
 * @date 2022/8/11
 */
class ServicePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply(JavaPlugin.class)
        project.pluginManager.apply(ModulePlugin.class)
        project.pluginManager.apply(BootPlugin.class)
    }
}
