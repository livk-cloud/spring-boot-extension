description = "spring boot extension lock"

dependencies {
	api(project(":spring-extension-context:spring-extension-lock"))
	optional(project(":spring-boot-extension-autoconfigure:spring-boot-extension-curator"))
	optional(project(":spring-boot-extension-autoconfigure:spring-boot-extension-redisson"))

	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("com.redis:testcontainers-redis")
	testImplementation("org.springframework.boot:spring-boot-data-redis")
}
