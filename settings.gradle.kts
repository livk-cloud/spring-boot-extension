plugins {
	id("com.gradle.develocity") version ("3.17.4")
}

develocity {
	buildScan {
		termsOfUseUrl = "https://gradle.com/terms-of-service"
		termsOfUseAgree = "yes"
		publishing {
			onlyIf {
				it.buildResult.failures.isNotEmpty()
			}
		}
	}
}

rootProject.name = "spring-boot-extension"
fileTree(rootDir) {
	include("**/*.gradle.kts")
	exclude("build", "**/gradle", "settings.gradle.kts", "buildSrc", "/build.gradle.kts", ".", "out")
}.forEach {
	val projectPath = it.parentFile.absolutePath
		.replace(rootDir.absolutePath, "")
		.replace(File.separator, ":")
	include(projectPath)
	project(projectPath).projectDir = it.parentFile
	project(projectPath).buildFileName = it.name
}

gradle.settingsEvaluated {
	if (JavaVersion.current() < JavaVersion.VERSION_21) {
		throw GradleException("This build requires JDK 21. It's currently ${JavaVersion.current()}.")
	}
}
