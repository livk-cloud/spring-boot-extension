package com.livk.plugin

import io.spring.javaformat.gradle.tasks.Format
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.compile.JavaCompile

/**
 * <p>
 * ResourcesPlugin
 * </p>
 *
 * @author livk
 * @date 2022/4/24
 */
class ResourcesPlugin implements Plugin<Project> {

    private static final List<String> COMPILER_ARGS

    static {
        COMPILER_ARGS = new ArrayList<>()
        COMPILER_ARGS.addAll(Arrays.asList(
                "-Xlint:-options", "-Xlint:rawtypes",
                "-Xlint:deprecation", "-Xlint:unchecked",))
    }

    @Override
    void apply(Project project) {
        def javaCompile = project.tasks
                .withType(JavaCompile.class)
                .getByName(JavaPlugin.COMPILE_JAVA_TASK_NAME)
        javaCompile.dependsOn(Format.NAME,JavaPlugin.PROCESS_RESOURCES_TASK_NAME)
        javaCompile.options.compilerArgs = COMPILER_ARGS
        javaCompile.options.encoding = "UTF-8"
    }
}
