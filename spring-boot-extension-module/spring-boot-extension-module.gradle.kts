plugins {
	com.livk.root
	`java-library`
}

subprojects {
	apply(plugin = "com.livk.common")
	apply(plugin = "com.livk.mvn.deployed")
	apply(plugin = "com.livk.jacoco")

	dependencies {
		api("org.springframework.boot:spring-boot-autoconfigure")

		aptCompile(project(":spring-auto-service"))
	}
}
