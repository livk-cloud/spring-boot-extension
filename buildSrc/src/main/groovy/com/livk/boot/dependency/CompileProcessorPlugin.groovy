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

package com.livk.boot.dependency

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * <p>
 * CompileProcessorPlugin
 * </p>
 *
 * @author livk
 *
 */
abstract class CompileProcessorPlugin implements Plugin<Project> {

    private static final String COMPILE_PROCESSOR = "compileProcessor"

    private static final Set<String> DEPENDENCY_NAMES_SET = new HashSet<>()

    static {
        DEPENDENCY_NAMES_SET.addAll(Set.of(
                JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME,
                JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME,
                JavaPlugin.TEST_COMPILE_CLASSPATH_CONFIGURATION_NAME,
                JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME
        ))
    }

    @Override
    void apply(Project project) {
        def configurations = project.configurations
        project.pluginManager.apply(JavaPlugin.class)
        configurations.create(COMPILE_PROCESSOR) { compileProcessor ->
            compileProcessor.visible = false
            compileProcessor.canBeResolved = false
            compileProcessor.canBeConsumed = false
            def plugins = project.plugins
            plugins.withType(JavaPlugin.class).every {
                DEPENDENCY_NAMES_SET.forEach { configurations.named(it).get().extendsFrom(compileProcessor) }
            }
        }
    }
}
