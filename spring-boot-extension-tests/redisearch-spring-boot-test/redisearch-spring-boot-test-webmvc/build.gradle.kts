plugins {
	com.livk.service
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation(project(":spring-boot-extension-starters:redisearch-spring-boot-starter"))

	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("com.redis:testcontainers-redis")
	testImplementation("org.springframework.boot:spring-boot-data-redis")
	testImplementation("org.springframework.boot:spring-boot-webmvc-test")
}
