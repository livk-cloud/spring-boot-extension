plugins {
	com.livk.bom
}

description = "spring boot extension bom"

dependencies {
	constraints {
		api(project(":spring-auto-service"))
		api(project(":spring-extension-commons"))
		api(project(":spring-extension-core"))
		api(project(":spring-boot-extension-autoconfigure"))
		project(":spring-boot-extension-starters").dependencyProject.subprojects {
			api(this)
		}
	}
}
