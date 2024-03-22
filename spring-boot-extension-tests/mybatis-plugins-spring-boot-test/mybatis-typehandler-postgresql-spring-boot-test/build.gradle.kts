plugins {
    com.livk.service
}

dependencies {
    implementation(project(":spring-boot-extension-starters:mybatis-plugins-spring-boot-starter"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.postgresql:postgresql")

	testImplementation("org.testcontainers:postgresql")
	testImplementation(project(":spring-extension-testcontainers"))
}
