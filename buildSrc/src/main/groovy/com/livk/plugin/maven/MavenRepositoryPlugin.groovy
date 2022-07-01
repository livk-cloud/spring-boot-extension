package com.livk.plugin.maven

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

/**
 * <p>
 * MavenRepositoryPlugin
 * </p>
 *
 * @author livk
 * @date 2022/7/1
 */
abstract class MavenRepositoryPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.pluginManager.apply(MavenPublishPlugin.class)
        def publishing = project.extensions.getByType(PublishingExtension.class)
        publishing.repositories.mavenLocal()
    }
}
