description = "spring boot extension limit"

dependencies {
	api("org.redisson:redisson")
	api("org.springframework:spring-aop")
	api("jakarta.servlet:jakarta.servlet-api")

	testImplementation(project(":spring-testcontainers-support"))
}
