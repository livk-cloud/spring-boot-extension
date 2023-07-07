plugins {
	com.livk.common
}

dependencies {
	api(project(":spring-extension-commons"))
	api("org.springframework.boot:spring-boot-starter")
	api("com.fasterxml.jackson.core:jackson-databind")
	api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	compileProcessor("com.google.auto.service:auto-service")
	compileProcessor(project(":spring-auto-service"))
}
