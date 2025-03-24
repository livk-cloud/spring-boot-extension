plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-extension-starters:distributed-lock-boot-starter"))
	implementation(project(":spring-boot-extension-starters:redisson-spring-boot-starter"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")

	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("com.redis:testcontainers-redis")
}
