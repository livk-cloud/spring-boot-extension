plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-extension-starters:distributed-lock-boot-starter"))
	implementation(project(":spring-boot-extension-starters:curator-spring-boot-starter"))
	implementation("org.springframework.boot:spring-boot-starter-web")

	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("org.springframework.boot:spring-boot-webmvc-test")
}
