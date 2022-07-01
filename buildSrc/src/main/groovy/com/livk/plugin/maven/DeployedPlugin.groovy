package com.livk.plugin.maven

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlatformPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.bundling.Jar

/**
 * <p>
 * DeployedPlugin
 * </p>
 *
 * @author livk
 * @date 2022/7/1
 */
abstract class DeployedPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def publication = publication(project)
        project.afterEvaluate { evaluated ->
            project.plugins.withType(JavaPlugin.class).all {
                if (((Jar) project.tasks.getByName(JavaPlugin.JAR_TASK_NAME)).isEnabled()) {
                    project.components
                            .matching { softwareComponent -> softwareComponent.name == "java" }
                            .every { publication.from(it) }
                }
            }
        }
        project.plugins.withType(JavaPlatformPlugin.class).every {
            project.components
                    .matching { softwareComponent -> softwareComponent.name == "javaPlatform" }
                    .every { publication.from(it) }
        }
    }

    static MavenPublication publication(Project project) {
        project.pluginManager.apply(MavenPublishPlugin.class)
        project.pluginManager.apply(MavenRepositoryPlugin.class)
        def publishing = project.extensions.getByType(PublishingExtension.class)
        return publishing.publications.create("maven", MavenPublication.class)
    }
}
