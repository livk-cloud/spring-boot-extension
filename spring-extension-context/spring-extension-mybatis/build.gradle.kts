description = "spring boot extension mybatis"

dependencies {
	api("org.mybatis:mybatis-spring")
	api("org.mybatis:mybatis")
	api("org.springframework:spring-context")
	optional("org.postgresql:postgresql")

	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("com.h2database:h2")
	testImplementation("com.mysql:mysql-connector-j")
	testImplementation("org.postgresql:postgresql")
	testImplementation("com.zaxxer:HikariCP")
	testImplementation("org.testcontainers:testcontainers-postgresql")
	testImplementation("org.testcontainers:testcontainers-mysql")
	testImplementation("org.springframework.boot:spring-boot-jdbc")
}
