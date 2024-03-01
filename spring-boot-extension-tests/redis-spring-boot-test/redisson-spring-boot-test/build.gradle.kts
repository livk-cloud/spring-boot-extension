plugins {
    com.livk.service
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(project(":spring-boot-extension-starters:redisson-spring-boot-starter"))

	testImplementation(project(":spring-extension-testcontainers"))
}
