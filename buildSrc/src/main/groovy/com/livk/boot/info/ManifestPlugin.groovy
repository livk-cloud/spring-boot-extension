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

package com.livk.boot.info

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.util.GradleVersion

/**
 * <p>
 * JarPlugin
 * </p>
 *
 * @author livk
 *
 */
abstract class ManifestPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.pluginManager.apply(JavaPlugin.class)
        project.tasks.withType(Jar.class).every {
            def attributes = it.manifest.attributes
            attributes.putIfAbsent("Implementation-Group", project.group)
            attributes.putIfAbsent("Implementation-Title", project.name)
            attributes.putIfAbsent("Implementation-Version", project.version)
            attributes.putIfAbsent("Created-By", System.getProperty("java.version") + " (" + System.getProperty("java.specification.vendor") + ")")
            attributes.putIfAbsent("Gradle-Version", GradleVersion.current())
        }
    }
}
