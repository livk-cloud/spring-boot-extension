description = "spring boot extension redisearch"

dependencies {
	api("com.redis:lettucemod")
	api("net.bytebuddy:byte-buddy")
	api("org.apache.commons:commons-pool2")
	api("org.springframework:spring-beans")

	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("com.redis:testcontainers-redis")
	testImplementation("org.springframework.boot:spring-boot-data-redis")
}
