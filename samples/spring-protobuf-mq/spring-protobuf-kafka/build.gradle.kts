plugins {
	com.livk.service
}

dependencies {
	implementation(project(":samples:spring-protobuf-mq:protobuf-commons"))
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testImplementation("org.testcontainers:kafka")
	testImplementation(project(":spring-testcontainers-support"))
}
