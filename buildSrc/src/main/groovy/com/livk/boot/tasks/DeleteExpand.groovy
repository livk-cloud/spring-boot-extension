package com.livk.boot.tasks

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.language.base.plugins.LifecycleBasePlugin

/**
 * <p>
 * DeleteExpand
 * </p>
 *
 * @author livk
 * @date 2022/7/25
 */
abstract class DeleteExpand implements Plugin<Project> {

    public static final String CLEAN_GENERATED_TASK_NAME = "cleanGenerated"

    public static final String CLEAN_OTHER_TASK_NAME = "cleanOther"

    public static final String CLEAN_ALL_TASK_NAME = "cleanAll"

    @Override
    void apply(Project project) {
        project.tasks.create(CLEAN_GENERATED_TASK_NAME, Delete.class) {
            setGroup(LifecycleBasePlugin.BUILD_GROUP)
            it.delete(project.projectDir.absolutePath + "/src/main/generated")
            it.delete(project.projectDir.absolutePath + "/src/test/generated_tests")
        }

        project.tasks.create(CLEAN_OTHER_TASK_NAME, Delete.class) {
            setGroup(LifecycleBasePlugin.BUILD_GROUP)
            it.delete(project.projectDir.absolutePath + "/build")
            it.delete(project.projectDir.absolutePath + "/out")
            it.delete(project.projectDir.absolutePath + "/bin")
        }

        project.tasks.create(CLEAN_ALL_TASK_NAME, Delete.class) {
            setGroup(LifecycleBasePlugin.BUILD_GROUP)
        }.dependsOn(CLEAN_GENERATED_TASK_NAME, CLEAN_OTHER_TASK_NAME)

    }
}
