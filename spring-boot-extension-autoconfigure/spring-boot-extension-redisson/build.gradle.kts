description = "spring boot extension sequence"

dependencies {
	api(project(":spring-extension-commons"))
	api("org.redisson:redisson")
	api("org.redisson:redisson-spring-data-40")
	api("tools.jackson.dataformat:jackson-dataformat-yaml")
	optional("org.springframework.boot:spring-boot-data-redis")

	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("com.redis:testcontainers-redis")
}
