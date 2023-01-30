package com.livk.boot.maven

import io.codearte.gradle.nexus.NexusStagingExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * <p>
 * NexusStagingPlugin
 * </p>
 *
 * @author livk
 * @date 2023/1/30
 */
abstract class NexusStagingPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply(io.codearte.gradle.nexus.NexusStagingPlugin.class)
        def nexusStaging = project.extensions.getByType(NexusStagingExtension.class)
        nexusStaging.username = project.property("mvn.username")
        nexusStaging.password = project.property("mvn.password")
        nexusStaging.numberOfRetries = 10
        nexusStaging.delayBetweenRetriesInMillis = 20000
    }
}
