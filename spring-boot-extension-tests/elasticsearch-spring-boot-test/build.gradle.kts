plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-extension-commons"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation(project(":spring-boot-extension-starters:elasticsearch-spring-boot-starter"))
	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("org.testcontainers:elasticsearch")
}
