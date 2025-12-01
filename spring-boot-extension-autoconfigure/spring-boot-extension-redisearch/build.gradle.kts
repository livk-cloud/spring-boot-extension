description = "spring boot extension redisearch"

dependencies {
	api(project(":spring-extension-context:spring-extension-redisearch"))
	api("org.springframework.boot:spring-boot-jackson")
	optional("org.springframework.boot:spring-boot-health")

	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("com.redis:testcontainers-redis")
	testImplementation("org.springframework.boot:spring-boot-data-redis")
}
