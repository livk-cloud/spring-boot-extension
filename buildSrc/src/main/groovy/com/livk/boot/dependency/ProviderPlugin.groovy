package com.livk.boot.dependency

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * <p>
 * ProviderPlugin
 * </p>
 *
 * @author livk
 * @date 2022/8/11
 */
abstract class ProviderPlugin implements Plugin<Project> {

    private static final String PROVIDER = "provider"

    private static final Set<String> DEPENDENCY_NAMES_SET = new HashSet<>()

    static {
        DEPENDENCY_NAMES_SET.addAll(Set.of(
                JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME,
                JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME,
                JavaPlugin.TEST_COMPILE_CLASSPATH_CONFIGURATION_NAME,
                JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME
        ))
    }

    @Override
    void apply(Project project) {
        def configurations = project.configurations
        project.pluginManager.apply(JavaPlugin.class)
        configurations.create(PROVIDER) { provider ->
            provider.visible = false
            provider.canBeResolved = false
            provider.canBeConsumed = false
            def plugins = project.plugins
            plugins.withType(JavaPlugin.class) {
                DEPENDENCY_NAMES_SET.forEach { configurations.getByName(it).extendsFrom(provider) }
            }
        }
    }
}
