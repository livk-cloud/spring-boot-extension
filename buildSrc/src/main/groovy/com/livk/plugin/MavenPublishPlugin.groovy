package com.livk.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.internal.DefaultPublishingExtension

/**
 * <p>
 * MavenPublishPlugin
 * </p>
 *
 * @author livk
 * @date 2022/6/28
 */
abstract class MavenPublishPlugin implements Plugin<Project> {

    public static final String Extension_NAME = "publishing"

    @Override
    void apply(Project project) {
        project.pluginManager.apply(MavenPublishPlugin.class)
        def extension = project.extensions.create(Extension_NAME, DefaultPublishingExtension.class)
        extension.repositories{
            it.mavenLocal()
        }
        extension.publications{

        }
    }
}
