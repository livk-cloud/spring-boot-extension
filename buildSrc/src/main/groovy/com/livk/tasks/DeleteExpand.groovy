package com.livk.tasks

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete

/**
 * <p>
 * DeleteExpand
 * </p>
 *
 * @author livk
 * @date 2022/7/25
 */
abstract class DeleteExpand implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.tasks.withType(Delete.class) {
            it.delete(project.projectDir.absolutePath + "/build")
            it.delete(project.projectDir.absolutePath + "/out")
            it.delete(project.projectDir.absolutePath + "/bin")
        }
    }
}
