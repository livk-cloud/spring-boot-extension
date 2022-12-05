package com.livk.boot.maven

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
 *
 */
abstract class MavenRepositoryPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.pluginManager.apply(MavenPublishPlugin.class)
        def publishing = project.extensions.getByType(PublishingExtension.class)
        publishing.repositories.mavenLocal()
        publishing.repositories.maven { maven ->
            def releasesRepoUrl = "http://localhost:8081/repository/maven-releases/"
            def snapshotsRepoUrl = "http://localhost:8081/repository/maven-snapshots/"
            //使用不安全的http请求、也就是缺失SSL
            maven.setAllowInsecureProtocol(true)
            maven.url = project.version.toString().endsWith("SNAPSHOT") ? snapshotsRepoUrl : releasesRepoUrl
            maven.credentials {
                it.username = "admin"
                it.password = "admin"
            }
        }
    }
}
