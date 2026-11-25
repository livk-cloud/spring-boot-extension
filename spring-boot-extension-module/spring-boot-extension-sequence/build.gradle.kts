description = "spring boot extension sequence"

dependencies {
	api(project(":spring-extension-context:spring-extension-sequence"))
	optional("org.springframework.boot:spring-boot-jdbc")
	optional("org.springframework.boot:spring-boot-data-redis")

	testImplementation("com.h2database:h2")
	testImplementation("com.mysql:mysql-connector-j")
	testImplementation("org.postgresql:postgresql")
	testImplementation("com.zaxxer:HikariCP")
	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("com.redis:testcontainers-redis")
	testImplementation("org.testcontainers:testcontainers-mysql")
	testImplementation("org.testcontainers:testcontainers-postgresql")
	testImplementation("org.springframework.boot:spring-boot-data-redis")
	testImplementation("org.springframework.boot:spring-boot-jdbc")
}
