package com.livk.boot

import com.livk.boot.dependency.CompileProcessorPlugin
import com.livk.boot.dependency.ManagementPlugin
import com.livk.boot.dependency.OptionalPlugin
import com.livk.boot.info.ManifestPlugin
import com.livk.boot.tasks.DeleteExpand
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * <p>
 * CorePlugin
 * </p>
 *
 * @author livk
 */
class CorePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply(DeleteExpand.class)
        project.pluginManager.apply(ManagementPlugin.class)
        project.pluginManager.apply(OptionalPlugin.class)
        project.pluginManager.apply(CompileProcessorPlugin.class)
        project.pluginManager.apply(ManifestPlugin.class)
    }
}
