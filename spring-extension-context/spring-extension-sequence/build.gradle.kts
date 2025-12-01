description = "spring boot extension sequence"

dependencies {
	api("com.lmax:disruptor")
	optional("org.springframework.data:spring-data-redis")
	optional("org.springframework.boot:spring-boot-jdbc")
	optional("org.springframework:spring-tx")
	optional("io.lettuce:lettuce-core")

	aptCompile(project(":spring-auto-service"))

	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("com.zaxxer:HikariCP")
	testImplementation("com.h2database:h2")
	testImplementation("com.mysql:mysql-connector-j")
	testImplementation("org.postgresql:postgresql")
	testImplementation("com.redis:testcontainers-redis")
	testImplementation("org.springframework.boot:spring-boot-data-redis")
	testImplementation("org.testcontainers:testcontainers-postgresql")
	testImplementation("org.testcontainers:testcontainers-mysql")
}
