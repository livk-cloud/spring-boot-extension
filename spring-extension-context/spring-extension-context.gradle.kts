plugins {
	com.livk.root
	`java-library`
}

subprojects {
	apply(plugin = "com.livk.common")
	apply(plugin = "com.livk.mvn.deployed")
	apply(plugin = "com.livk.jacoco")

	dependencies {
		api(project(":spring-extension-commons"))
		optional("ch.qos.logback:logback-classic")
	}
}
