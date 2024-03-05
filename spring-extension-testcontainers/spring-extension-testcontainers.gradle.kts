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
    optional("org.testcontainers:postgresql")
    optional("org.testcontainers:clickhouse")
    optional("org.testcontainers:minio")
    optional("org.testcontainers:r2dbc")
	optional("com.clickhouse:clickhouse-r2dbc")
}

