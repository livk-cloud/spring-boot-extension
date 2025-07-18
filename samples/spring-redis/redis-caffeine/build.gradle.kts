plugins {
	com.livk.service
}

dependencies {
	implementation(project(":samples:spring-redis:redis-commons"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("com.github.ben-manes.caffeine:caffeine")

	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("com.redis:testcontainers-redis")
}
