description = "spring boot extension curator"

dependencies {
	api("org.apache.curator:curator-recipes")

	testImplementation(project(":spring-testcontainers-support"))
}
