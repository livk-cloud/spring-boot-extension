plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-extension-commons"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.integration:spring-integration-stream")
	implementation("org.springframework.integration:spring-integration-mqtt")

	testImplementation("org.testcontainers:rabbitmq")
	testImplementation(project(":spring-extension-testcontainers"))
}
