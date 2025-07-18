plugins {
	com.livk.service
}

dependencies {
	implementation(project(":samples:spring-protobuf-mq:protobuf-commons"))
	implementation("org.springframework.boot:spring-boot-starter-pulsar")
	implementation("org.springframework.boot:spring-boot-starter-web")

	testImplementation("org.testcontainers:pulsar")
	testImplementation(project(":spring-testcontainers-support"))
}
