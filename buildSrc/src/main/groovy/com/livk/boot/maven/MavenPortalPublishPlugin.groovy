/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.boot.maven

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.MavenPublishPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author livk
 */
abstract class MavenPortalPublishPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.pluginManager.apply(MavenPublishPlugin)

		def group = project.group.toString()

		project.extensions.getByType(MavenPublishBaseExtension).tap {
			publishToMavenCentral()
			signAllPublications()
			coordinates(group, project.name, project.version.toString())

			pom { pom ->
				project.afterEvaluate {
					pom.name.set(project.name)
					pom.description.set(project.description)
				}
				pom.url.set("https://github.com/livk-cloud/${project.rootProject.name}")
				pom.licenses { licenses ->
					licenses.license { license ->
						license.name.set('The Apache License, Version 2.0')
						license.url.set('https://www.apache.org/licenses/LICENSE-2.0.txt')
					}
				}
				pom.developers { developers ->
					developers.developer { developer ->
						developer.name.set('livk')
						developer.email.set('livk.cloud@gmail.com')
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
