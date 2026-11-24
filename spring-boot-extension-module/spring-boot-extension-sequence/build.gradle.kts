description = "spring boot extension sequence"

dependencies {
	api(project(":spring-extension-context:spring-extension-sequence"))
	optional("org.springframework.boot:spring-boot-jdbc")
	optional("org.springframework.boot:spring-boot-data-redis")

	testImplementation("com.h2database:h2")
}
