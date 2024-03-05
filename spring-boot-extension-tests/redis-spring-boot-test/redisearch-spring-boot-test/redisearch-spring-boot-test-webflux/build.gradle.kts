plugins {
    com.livk.service
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation(project(":spring-boot-extension-starters:redisearch-spring-boot-starter"))

	testImplementation(project(":spring-extension-testcontainers"))
}
