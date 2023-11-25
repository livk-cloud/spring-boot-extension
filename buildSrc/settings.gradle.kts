dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			from(files("../gradle/libs.versions.toml"))
		}
	}
}

gradle.settingsEvaluated {
	if (JavaVersion.current() < JavaVersion.VERSION_21) {
		throw GradleException("This build requires JDK 21. It's currently ${JavaVersion.current()}. You can ignore this check by passing '-Dorg.gradle.ignoreBuildJavaVersionCheck'.")
	}
}
