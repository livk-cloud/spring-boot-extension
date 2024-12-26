plugins {
    com.livk.common
}

description = "spring testcontainers support"

dependencies {
    api("org.testcontainers:junit-jupiter")
    api("org.testcontainers:testcontainers")
    api("org.springframework.boot:spring-boot-testcontainers")

	compileProcessor(project(":spring-auto-service"))

    optional("org.testcontainers:mysql")
    optional("org.testcontainers:kafka")
    optional("org.testcontainers:postgresql")
    optional("org.testcontainers:clickhouse")
    optional("org.testcontainers:minio")
    optional("org.testcontainers:r2dbc")
	optional(libs.clickhouse.r2dbc)

	testImplementation("com.mysql:mysql-connector-j")
	testImplementation("org.postgresql:postgresql")
}

