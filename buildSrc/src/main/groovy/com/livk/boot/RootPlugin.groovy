package com.livk.boot

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.bundling.Jar

/**
 * <p>
 * RootProjectPlugin
 * </p>
 *
 * @author livk
 *
 */
class RootPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply(BasePlugin.class)
        project.pluginManager.apply(CorePlugin.class)

        project.tasks.withType(Jar.class).configureEach {
            it.enabled = false
        }
    }
}
