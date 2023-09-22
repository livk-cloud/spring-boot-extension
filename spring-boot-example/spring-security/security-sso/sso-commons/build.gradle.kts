plugins {
	com.livk.common
}

dependencies {
	api(project(":spring-extension-commons"))
	api("org.springframework.boot:spring-boot-autoconfigure")
	api(libs.nimbus.jose.jwt)
	optional("org.springframework:spring-core")
	optional("org.springframework.security:spring-security-core")
	optional("org.springframework.security:spring-security-web")
	optional("jakarta.servlet:jakarta.servlet-api")
	api("com.fasterxml.jackson.core:jackson-databind")
	compileProcessor(project(":spring-auto-service"))
}
