plugins {
	com.livk.root
	`java-library`
}

subprojects {
	apply(plugin = "com.livk.common")
	apply(plugin = "com.livk.mvn.deployed")

	dependencies {
		api("org.springframework.boot:spring-boot-starter")
	}
}
