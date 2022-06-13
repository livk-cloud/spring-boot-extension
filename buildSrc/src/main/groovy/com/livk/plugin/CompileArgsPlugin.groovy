package com.livk.plugin


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.compile.JavaCompile

/**
 * <p>
 * 添加编译参数
 * </p>
 *
 * @author livk
 * @date 2022/6/6
 */
abstract class CompileArgsPlugin implements Plugin<Project> {

    private static final List<String> COMPILER_ARGS = new ArrayList<>()
    private static final String MAPSTRUCT_NAME = "mapstruct"
    private static final String MAPSTRUCT_PROCESSOR_NAME = "mapstruct-processor"
    private static final List<String> MAPSTRUCT_COMPILER_ARGS = new ArrayList<>()
    private static final String UTF_8 = "UTF-8"

    static {
        COMPILER_ARGS.addAll(Arrays.asList("-Xlint:-options",
                "-Xlint:rawtypes",
                "-Xlint:deprecation",
                "-Xlint:unchecked",))
        MAPSTRUCT_COMPILER_ARGS.addAll(Arrays.asList("-Amapstruct.unmappedTargetPolicy=IGNORE"))
    }

    @Override
    void apply(Project project) {
        project.plugins.apply(JavaPlugin.class)
        def javaCompile = project.tasks
                .getByName(JavaPlugin.COMPILE_JAVA_TASK_NAME) as JavaCompile
        javaCompile.options.compilerArgs.addAll(COMPILER_ARGS)
        javaCompile.options.encoding = UTF_8
        //在 Project 配置结束后调用
        project.afterEvaluate {
            def dependencyName = new HashSet<>()
            project.configurations.forEach {
                dependencyName.addAll(it.dependencies.name)
            }
            if (dependencyName.contains(MAPSTRUCT_NAME) || dependencyName.contains(MAPSTRUCT_PROCESSOR_NAME)) {
                javaCompile.options.compilerArgs.addAll(MAPSTRUCT_COMPILER_ARGS)
            }
        }
    }
}
