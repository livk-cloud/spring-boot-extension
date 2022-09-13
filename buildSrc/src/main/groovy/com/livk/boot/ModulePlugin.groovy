package com.livk.boot

import com.livk.boot.compile.CompileArgsPlugin
import com.livk.boot.config.AllConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
/**
 * <p>
 * ModulePlugin
 * </p>
 *
 * @author livk
 * @date 2022/8/11
 */
class ModulePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply(CompileArgsPlugin.class)
        project.pluginManager.apply(AllConfiguration.class)
        project.pluginManager.apply(RootProjectPlugin.class)
    }
}
