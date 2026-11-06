plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-extension-commons"))
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation(project(":spring-boot-extension-starters:curator-spring-boot-starter"))

	testImplementation(project(":spring-testcontainers-support"))
}
