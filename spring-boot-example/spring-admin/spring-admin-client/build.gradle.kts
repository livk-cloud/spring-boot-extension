plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-extension-commons"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("de.codecentric:spring-boot-admin-starter-client")
}
