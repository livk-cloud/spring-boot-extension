/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.boot.maven

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlatformPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.plugins.signing.SigningExtension
import org.gradle.plugins.signing.SigningPlugin

/**
 * <p>
 * DeployedPlugin
 * </p>
 *
 * @author livk
 *
 */
abstract class DeployedPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.pluginManager.apply(SigningPlugin.class)
        def publication = publication(project)
        def signing = project.extensions.getByType(SigningExtension.class)
        signing.setRequired(false)
        signing.sign(publication)
        project.afterEvaluate { evaluated ->
            project.plugins.withType(JavaPlugin.class).every {
                if ((project.tasks.named(JavaPlugin.JAR_TASK_NAME).get() as Jar).isEnabled()) {
                    def javaPluginExtension = project.extensions.getByType(JavaPluginExtension.class)
                    javaPluginExtension.withSourcesJar()
                    javaPluginExtension.withJavadocJar()
                    project.components
                            .matching { softwareComponent -> softwareComponent.name == "java" }
                            .every { publication.from(it) }
                    mavenInfo(publication, project)
                }
            }
        }
        project.plugins.withType(JavaPlatformPlugin.class).every {
            project.components
                    .matching { softwareComponent -> softwareComponent.name == "javaPlatform" }
                    .every { publication.from(it) }
            mavenInfo(publication, project)
        }
    }

    static MavenPublication publication(Project project) {
        project.pluginManager.apply(MavenPublishPlugin.class)
        project.pluginManager.apply(MavenRepositoryPlugin.class)
        def publishing = project.extensions.getByType(PublishingExtension.class)
        return publishing.publications.create("maven", MavenPublication.class)
    }

    static void mavenInfo(MavenPublication publication, Project project) {
        publication.versionMapping {
            allVariants {
                fromResolutionResult()
            }
        }
        publication.pom {
            name.set(project.name)
            def projectDescription = project.description
            if (projectDescription == null || projectDescription.isBlank()) {
                projectDescription = project.name.replaceAll("-", " ")
            }
            description.set(projectDescription)
            def rootProjectName = project.rootProject.name
            url.set("https://github.com/livk-cloud/" + rootProjectName + "/")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    name.set("livk")
                    email.set("1375632510@qq.com")
                }
            }
            scm {
                connection.set("git@github.com:livk-cloud/" + rootProjectName + ".git")
                url.set("https://github.com/livk-cloud/" + rootProjectName + "/")
            }
        }
    }
}
