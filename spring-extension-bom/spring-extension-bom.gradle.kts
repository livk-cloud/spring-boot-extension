plugins {
	com.livk.bom
}

description = "spring boot extension bom"

dependencies {
	constraints {
		api(project(":spring-auto-service"))
		api(project(":spring-extension-commons"))
		project(":spring-extension-context") {
			subprojects {
				api(this)
			}
		}
		project(":spring-boot-extension-autoconfigure") {
			subprojects {
				api(this)
			}
		}
		project(":spring-boot-extension-starters") {
			subprojects {
				api(this)
			}
		}
	}
}
