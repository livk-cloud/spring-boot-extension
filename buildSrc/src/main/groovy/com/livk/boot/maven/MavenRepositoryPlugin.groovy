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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

/**
 * @author livk
 */
abstract class MavenRepositoryPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.pluginManager.apply(MavenPublishPlugin)

		project.extensions.getByType(PublishingExtension).tap {
			repositories.mavenLocal()
			try {
				def releasesRepoUrl = project.property('mvn.releasesRepoUrl').toString()
				def snapshotsRepoUrl = project.property('mvn.releasesRepoUrl').toString()
				repositories.maven { maven ->
					maven.name = 'CustomizeMaven'
					maven.allowInsecureProtocol = true
					def url = project.version.toString().endsWith('SNAPSHOT') ? snapshotsRepoUrl : releasesRepoUrl
					maven.url = url
					maven.credentials {
						it.username = project.property('mvn.username').toString()
						it.password = project.property('mvn.password').toString()
					}
				}
			} catch (Exception ignored) {
				// 忽略异常
			}
		}
	}
}
