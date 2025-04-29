plugins {
	com.livk.bom
}

description = "spring boot extension bom"

dependencies {
	constraints {
		api(project(":spring-auto-service"))
		api(project(":spring-extension-commons"))
		api(project(":spring-extension-context"))
		api(project(":spring-boot-extension-autoconfigure"))
		project(":spring-boot-extension-starters") {
			subprojects {
				api(this)
			}
		}
	}
}
