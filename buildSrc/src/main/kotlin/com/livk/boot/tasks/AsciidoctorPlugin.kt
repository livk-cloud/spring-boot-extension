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

package com.livk.boot.tasks

import org.asciidoctor.gradle.model5.core.AsciidoctorModelExtension
import org.asciidoctor.gradle.model5.jvm.formatters.AsciidoctorjGenericOutputFormatter
import org.asciidoctor.gradle.model5.jvm.plugins.AsciidoctorjPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.invoke
import java.io.File
import java.time.LocalDate

/**
 * <p>
 * AsciidoctorPlugin
 * </p>
 *
 * @author livk
 * @date 2026/11/19
 */
abstract class AsciidoctorPlugin : Plugin<Project> {

	override fun apply(project: Project) {
		project.pluginManager.apply(AsciidoctorjPlugin::class.java)
		val docsDir = project.layout.projectDirectory.dir("docs")
		project.extensions.configure(AsciidoctorModelExtension::class.java) {
			toolchains.named("asciidoctorj") {
				registeredOutputFormatters {
					create("spring", AsciidoctorjGenericOutputFormatter::class.java) {
						setBackend("spring-html")
						useModule("io.spring.asciidoctor.backends:spring-asciidoctor-backends", "0.0.7")
					}
				}
			}
			publications {
				named("main") {
					sourceSet {
						setSourceDir(docsDir.dir("asciidoctor"))
						sources {
							include("spring-boot-extension.adoc")
						}
						attributes {
							add("revnumber", project.provider { project.version.toString() })
							addAsDate("docdate", project.provider { LocalDate.now() })
						}
					}
					output("asciidoctorj", "spring")
				}
			}
		}

		val asciidoctorSpring = project.tasks.named("asciidoctorSpring")
		val docVersion = project.provider { project.version.toString().removeSuffix("-SNAPSHOT") }
		val docsSeriesDir = docVersion.map { versionText ->
			versionText.split('.').take(2).joinToString(separator = ".", postfix = ".x")
		}

		project.tasks.register("asciidoctorSync", Copy::class.java) {
			group = "documentation"
			description = "Publishes Asciidoctor Spring HTML into docs/<major.minor.x>/"
			dependsOn(asciidoctorSpring)
			duplicatesStrategy = DuplicatesStrategy.INCLUDE
			into(docsDir)
			from(asciidoctorSpring) {
				include("css/**", "js/**", "img/**")
			}

			from(docsDir.file("img/banner-logo-copy.svg")) {
				into("img")
				rename { "banner-logo.svg" }
			}

			from(asciidoctorSpring) {
				include("spring-boot-extension.html")
				into(docsSeriesDir)
				rename { "spring-boot-extension-${docVersion.get()}.html" }
				filter { line: String ->
					line.replace("""href="css/""", """href="../css/""")
						.replace("""src="js/""", """src="../js/""")
						.replace("""href="index.html""", """href="../index.html""")
						.replace(
							Regex("""(Last updated \d{4}-\d{2}-\d{2})(?:\s+\d{2}:\d{2}:\d{2})?\s*[+-]\d{4}"""),
							"$1"
						)
				}
			}

			doLast {
				renderDocsIndex(docsDir.asFile)
			}
		}
	}

	private fun renderDocsIndex(docsDir: File) {
		val seriesList = scanPublishedDocs(docsDir)
		check(seriesList.isNotEmpty()) {
			"No published docs found under ${docsDir.absolutePath}"
		}

		val latest = seriesList.asSequence()
			.flatMap { series -> series.versions.asSequence().map { version -> series to version } }
			.maxWith(compareBy(VERSION_COMPARATOR) { it.second.comparable })

		val template = File(docsDir, "templates/index.html").readText()
		val sections = renderVersionSections(seriesList)
		val html = template
			.replace("{{LATEST_HREF}}", latest.second.href)
			.replace("{{LATEST_VERSION}}", latest.second.label)
			.replace("{{VERSION_SECTIONS}}", sections)

		File(docsDir, "index.html").writeText(html)
	}

	private fun scanPublishedDocs(docsDir: File): List<DocSeries> {
		val versionFile = Regex("""^spring-boot-extension-(.+)\.html$""")
		val seriesDir = Regex("""^\d+\.\d+\.x$""")

		return docsDir.listFiles()
			.orEmpty()
			.filter { it.isDirectory && seriesDir.matches(it.name) }
			.mapNotNull { dir ->
				val versions = dir.listFiles()
					.orEmpty()
					.mapNotNull { file ->
						val match = versionFile.matchEntire(file.name) ?: return@mapNotNull null
						val label = match.groupValues[1]
						DocVersion(
							label = label,
							href = "${dir.name}/${file.name}",
							comparable = parseVersion(label)
						)
					}
					.sortedWith(compareByDescending(VERSION_COMPARATOR) { it.comparable })
				if (versions.isEmpty()) {
					null
				}
				else {
					DocSeries(
						label = dir.name,
						comparable = parseSeries(dir.name),
						versions = versions
					)
				}
			}
			.sortedWith(compareByDescending(VERSION_COMPARATOR) { it.comparable })
	}

	private fun renderVersionSections(seriesList: List<DocSeries>): String {
		return seriesList.mapIndexed { index, series ->
			val links = series.versions.joinToString(separator = "\n") { version ->
				"""      <a href="${version.href}">${version.label}</a>"""
			}
			if (index == 0) {
				"""
    <section class="version-series current" aria-label="${series.label}">
      <div class="series-heading">
        <span class="series-label">${series.label}</span>
        <span class="series-meta">当前系列 · ${series.versions.size} 个版本</span>
      </div>
      <div class="version-links">
$links
      </div>
    </section>
""".trimEnd()
			}
			else {
				"""
    <details class="version-series">
      <summary>
        <span class="series-label">${series.label}</span>
        <span class="series-meta">${series.versions.size} 个版本</span>
      </summary>
      <div class="version-links">
$links
      </div>
    </details>
""".trimEnd()
			}
		}.joinToString(separator = "\n")
	}

	private fun parseVersion(version: String): List<Int> {
		return version.removeSuffix("-SNAPSHOT")
			.split('.')
			.map { part -> part.toIntOrNull() ?: 0 }
	}

	private fun parseSeries(series: String): List<Int> {
		return series.removeSuffix(".x")
			.split('.')
			.map { part -> part.toIntOrNull() ?: 0 }
	}

	private data class DocSeries(
		val label: String,
		val comparable: List<Int>,
		val versions: List<DocVersion>,
	)

	private data class DocVersion(
		val label: String,
		val href: String,
		val comparable: List<Int>,
	)

	companion object {
		private val VERSION_COMPARATOR = Comparator<List<Int>> { left, right ->
			val size = maxOf(left.size, right.size)
			for (index in 0 until size) {
				val cmp = left.getOrElse(index) { 0 }.compareTo(right.getOrElse(index) { 0 })
				if (cmp != 0) {
					return@Comparator cmp
				}
			}
			0
		}
	}
}
