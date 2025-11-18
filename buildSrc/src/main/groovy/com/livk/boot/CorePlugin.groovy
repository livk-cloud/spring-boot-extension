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

package com.livk.boot

import com.livk.boot.dependency.AptCompilePlugin
import com.livk.boot.dependency.ManagementPlugin
import com.livk.boot.dependency.OptionalPlugin
import com.livk.boot.info.ManifestPlugin
import com.livk.boot.tasks.DeleteExpand
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.plugins.quality.CheckstylePlugin

/**
 * @author livk
 */
class CorePlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.pluginManager.apply(DeleteExpand)
		project.pluginManager.apply(ManagementPlugin)
		project.pluginManager.apply(OptionalPlugin)
		project.pluginManager.apply(AptCompilePlugin)
		project.pluginManager.apply(ManifestPlugin)
		project.pluginManager.apply(CheckstylePlugin)

		project.extensions.getByType(CheckstyleExtension).with {
			def checkstyleVersion = project.rootProject
				.extensions
				.getByType(VersionCatalogsExtension)
				.named('libs')
				.findVersion('checkstyle')
				.get()
				.displayName
			toolVersion = checkstyleVersion
			configFile = new File("${project.rootDir.path}/src/checkstyle/checkstyle.xml")
		}
	}
}

