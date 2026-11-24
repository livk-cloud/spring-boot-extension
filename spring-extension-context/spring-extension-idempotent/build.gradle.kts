description = "spring boot extension idempotent"

dependencies {
	api("org.redisson:redisson")
	api("org.springframework:spring-aop")

	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("com.redis:testcontainers-redis")
}
