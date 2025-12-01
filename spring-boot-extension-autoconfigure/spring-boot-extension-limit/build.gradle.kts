description = "spring boot extension limit"

dependencies {
	api(project(":spring-extension-context:spring-extension-limit"))
	api(project(":spring-boot-extension-autoconfigure:spring-boot-extension-redisson"))

	testImplementation("com.redis:testcontainers-redis")
	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("org.springframework.boot:spring-boot-data-redis")
}
