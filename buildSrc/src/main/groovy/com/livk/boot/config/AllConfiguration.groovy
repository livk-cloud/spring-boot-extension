package com.livk.boot.config


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension

/**
 * <p>
 * AllConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/7/25
 */
abstract class AllConfiguration implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def springbootVersion = project.rootProject
                .extensions
                .getByType(VersionCatalogsExtension.class)
                .named("libs")
                .findVersion("springBoot")
                .get()
                .displayName
        project.configurations.every { config ->
            config.resolutionStrategy { strategy ->
                strategy.dependencySubstitution { dependency ->
                    dependency.substitute(dependency.module("org.springframework.boot:spring-boot-starter-tomcat"))
                            .using(dependency.module("org.springframework.boot:spring-boot-starter-undertow:" + springbootVersion))
                }
            }
        }
    }
}
