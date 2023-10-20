plugins {
	com.livk.common
}

dependencies {
	api(project(":spring-extension-commons"))
	api("org.springframework.boot:spring-boot-starter-amqp")
	api("org.springframework.amqp:spring-rabbit-stream")
	api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	compileProcessor(project(":spring-auto-service"))
}
