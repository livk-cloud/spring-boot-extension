package com.livk.boot.maven

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.MavenPublishPlugin
import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * <p>
 * MavenPortalPublishPlugin
 * </p>
 *
 * @author livk
 * @date 2025/4/28
 */
abstract class MavenPortalPublishPlugin : Plugin<Project> {

	override fun apply(project: Project) {
		project.pluginManager.apply(MavenPublishPlugin::class.java)

		val group = project.group.toString()

		project.extensions.getByType(MavenPublishBaseExtension::class.java).apply {

			publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

			signAllPublications()

			coordinates(group, project.name, project.version.toString())

			pom { pom ->
				project.afterEvaluate {
					pom.name.set(project.name)
					pom.description.set(project.description)
				}
				pom.url.set("https://github.com/livk-cloud/" + project.rootProject.name)
				pom.licenses { licenses ->
					licenses.license { license ->
						license.name.set("The Apache License, Version 2.0")
						license.url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
					}
				}
				pom.developers { developers ->
					developers.developer { developer ->
						developer.name.set("livk")
						developer.email.set("livk.cloud@gmail.com")
					}
				}
				pom.scm { scm ->
					scm.connection.set("git@github.com:livk-cloud/${project.rootProject.name}.git")
					scm.url.set("https://github.com/livk-cloud/${project.rootProject.name}/")
				}
			}
		}
	}
}
