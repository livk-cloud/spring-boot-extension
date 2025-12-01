description = "spring boot extension lock"

dependencies {
	api("org.springframework:spring-aop")
	optional("org.apache.curator:curator-recipes")
	optional("org.redisson:redisson")

	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("com.redis:testcontainers-redis")
	testImplementation("org.springframework.boot:spring-boot-data-redis")
}
