plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-extension-commons"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.integration:spring-integration-stream")
	implementation("org.springframework.integration:spring-integration-mqtt")
	implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")

	testImplementation("org.testcontainers:rabbitmq")
	testImplementation(project(":spring-testcontainers-support"))
}
