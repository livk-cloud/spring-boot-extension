package com.livk.boot.info

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.bundling.Jar
import org.springframework.boot.gradle.dsl.SpringBootExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar

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
abstract class BootPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply(JavaPlugin.class)
        project.pluginManager.apply(SpringBootPlugin.class)
        project.extensions
                .getByType(SpringBootExtension.class)
                .buildInfo {
                    it.properties {
                        it.group = project.group
                        it.version = project.version
                        it.time = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8))
                    }
                }
        def bootJar = project.tasks.getByName(SpringBootPlugin.BOOT_JAR_TASK_NAME) as BootJar
        bootJar.archiveBaseName.set(project.name)
        bootJar.archiveFileName.set(bootJar.archiveBaseName.get() + "." + bootJar.archiveExtension.get())
        bootJar.launchScript()
        (project.tasks.getByName(JavaPlugin.JAR_TASK_NAME) as Jar).enabled = false
    }
}
