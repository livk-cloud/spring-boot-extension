plugins {
	com.livk.service
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-integration")
	implementation("org.springframework.integration:spring-integration-zookeeper")
	implementation(project(":spring-boot-extension-starters:curator-spring-boot-starter"))

	testImplementation(project(":spring-testcontainers-support"))
}
