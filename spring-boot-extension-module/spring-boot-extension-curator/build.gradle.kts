description = "spring boot extension curator"

dependencies {
	api(project(":spring-extension-context:spring-extension-curator"))
	optional("org.springframework.boot:spring-boot-health")

	testImplementation(project(":spring-testcontainers-support"))
}
