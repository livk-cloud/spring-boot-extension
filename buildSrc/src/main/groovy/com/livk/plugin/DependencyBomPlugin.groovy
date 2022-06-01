package com.livk.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension

/**
 * <p>
 * DependencyBom
 * </p>
 *
 * @author livk
 * @date 2022/6/1
 */
abstract class DependencyBomPlugin implements Plugin<Project> {

    public static final String DEPENDENCY_BOM = "dependencyBom"

    @Override
    void apply(Project project) {
        def dependencyBom = project.configurations.create(DEPENDENCY_BOM)
        dependencyBom.canBeResolved = false
        dependencyBom.canBeConsumed = false
        project.plugins.withType(JavaPlugin.class) {
            project.extensions.getByType(JavaPluginExtension.class).sourceSets.every {
                project.configurations.getByName(JavaPlugin.API_CONFIGURATION_NAME).extendsFrom(dependencyBom)
                project.configurations.getByName(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME).extendsFrom(dependencyBom)
                project.configurations.getByName(JavaPlugin.API_ELEMENTS_CONFIGURATION_NAME).extendsFrom(dependencyBom)
                project.configurations.getByName(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME).extendsFrom(dependencyBom)
                project.configurations.getByName(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME).extendsFrom(dependencyBom)
                project.configurations.getByName(JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME).extendsFrom(dependencyBom)
                project.configurations.getByName(JavaPlugin.TEST_COMPILE_ONLY_CONFIGURATION_NAME).extendsFrom(dependencyBom)
                project.configurations.getByName(JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME).extendsFrom(dependencyBom)
            }
        }
    }
}
