plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-extension-commons"))
	implementation("org.springframework.boot:spring-boot-starter-pulsar")
	implementation("org.springframework.boot:spring-boot-starter-web")

	testImplementation("org.testcontainers:pulsar")
	testImplementation(project(":spring-extension-testcontainers"))
}
