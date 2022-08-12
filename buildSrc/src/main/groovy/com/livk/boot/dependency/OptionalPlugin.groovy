package com.livk.boot.dependency

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension

/**
 * <p>
 * OptionalPlugin
 * </p>
 *
 * @author livk
 * @date 2022/8/12
 */
abstract class OptionalPlugin implements Plugin<Project> {

    public static final String OPTIONAL = "optional"

    @Override
    void apply(Project project) {
        def configurations = project.configurations
        def optional = configurations.create(OPTIONAL)
        optional.canBeConsumed = false
        optional.canBeResolved = false
        project.getPlugins().withType(JavaPlugin.class) {
            def sourceSets = project.extensions.getByType(JavaPluginExtension.class).sourceSets
            sourceSets.every { sourceSet ->
                configurations.getByName(sourceSet.getCompileClasspathConfigurationName()).extendsFrom(optional)
                configurations.getByName(sourceSet.getRuntimeClasspathConfigurationName()).extendsFrom(optional)
            }
        }
    }
}
