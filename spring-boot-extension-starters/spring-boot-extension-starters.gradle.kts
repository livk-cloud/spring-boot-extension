plugins {
	com.livk.root
	`java-library`
}

subprojects {
	apply(plugin = "com.livk.common")
	apply(plugin = "com.livk.mvn.deployed")

	dependencies {
		api(project(":spring-boot-extension-autoconfigure"))
		api("org.springframework.boot:spring-boot-starter")
	}
}
