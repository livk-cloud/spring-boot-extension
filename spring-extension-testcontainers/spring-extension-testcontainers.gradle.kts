plugins {
	com.livk.common
}

description = "spring testcontainers support"

dependencies {
	api("org.testcontainers:junit-jupiter")
	api("org.testcontainers:testcontainers")
	api("org.springframework.boot:spring-boot-testcontainers")

	optional("org.testcontainers:mysql")
	optional("org.testcontainers:postgresql")
}

