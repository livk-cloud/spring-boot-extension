plugins {
	com.livk.service
}

dependencies {
	implementation(project(":samples:spring-protobuf-mq:protobuf-commons"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-amqp")

	testImplementation("org.testcontainers:rabbitmq")
	testImplementation(project(":spring-testcontainers-support"))
}
