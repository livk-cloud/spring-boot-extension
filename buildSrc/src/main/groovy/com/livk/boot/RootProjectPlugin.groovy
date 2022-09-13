package com.livk.boot

import com.livk.boot.dependency.ManagementPlugin
import com.livk.boot.dependency.OptionalPlugin
import com.livk.boot.dependency.ProviderPlugin
import com.livk.boot.tasks.DeleteExpand
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin

/**
 * <p>
 * RootProjectPlugin
 * </p>
 *
 * @author livk
 * @date 2022/8/11
 */
class RootProjectPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply(BasePlugin.class)
        project.pluginManager.apply(DeleteExpand.class)
        project.pluginManager.apply(ManagementPlugin.class)
        project.pluginManager.apply(OptionalPlugin.class)
        project.pluginManager.apply(ProviderPlugin.class)
    }
}
