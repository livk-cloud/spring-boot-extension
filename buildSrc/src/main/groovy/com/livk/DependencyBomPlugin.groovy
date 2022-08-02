package com.livk

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaTestFixturesPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

/**
 * <p>
 * 创建dependency效果的BOM引入器
 * </p>
 *
 * @author livk
 * @date 2022/6/1
 */
abstract class DependencyBomPlugin implements Plugin<Project> {

    public static final String DEPENDENCY_BOM = "dependencyBom"

    public static final Set<String> DEPENDENCY_NAMES_SET = new HashSet<>()

    static {
        DEPENDENCY_NAMES_SET.addAll(
                Set.of(
                        JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME,
                        JavaPlugin.API_ELEMENTS_CONFIGURATION_NAME,
                        JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME,
                        JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME,
                        JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME,
                        JavaPlugin.TEST_COMPILE_ONLY_CONFIGURATION_NAME,
                        JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME
                )
        )
    }

    @Override
    void apply(Project project) {
        def configurations = project.configurations
        project.pluginManager.apply(JavaPlugin.class)
        configurations.create(DEPENDENCY_BOM) { dependencyBom ->
            dependencyBom.visible = false
            dependencyBom.canBeResolved = false
            dependencyBom.canBeConsumed = false
            def plugins = project.plugins
            plugins.withType(JavaPlugin.class) {
                DEPENDENCY_NAMES_SET.forEach { configurations.getByName(it).extendsFrom(dependencyBom) }
            }
            plugins.withType(JavaTestFixturesPlugin.class) {
                configurations.getByName("testFixturesCompileClasspath").extendsFrom(dependencyBom)
                configurations.getByName("testFixturesRuntimeClasspath").extendsFrom(dependencyBom)
            }
            plugins.withType(MavenPublishPlugin.class) {
                project.extensions
                        .getByType(PublishingExtension.class)
                        .publications
                        .withType(MavenPublication.class) { mavenPublication ->
                            mavenPublication.versionMapping { versions ->
                                versions.allVariants {
                                    it.fromResolutionResult()
                                }
                            }
                        }
            }
        }
    }
}
