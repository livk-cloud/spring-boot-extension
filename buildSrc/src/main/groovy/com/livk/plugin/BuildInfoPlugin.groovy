package com.livk.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.springframework.boot.gradle.dsl.SpringBootExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin

import java.time.Instant
import java.util.concurrent.TimeUnit

/**
 * <p>
 * BuildInfoPlugin
 * </p>
 *
 * @author livk
 * @date 2022/7/1
 */
abstract class BuildInfoPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply(SpringBootPlugin.class)
        def springBootExtension = project.extensions.getByType(SpringBootExtension.class)
        springBootExtension.buildInfo {
            it.properties {
                it.group = project.group
                it.version = project.version
                it.time = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8))
            }
        }
    }
}
