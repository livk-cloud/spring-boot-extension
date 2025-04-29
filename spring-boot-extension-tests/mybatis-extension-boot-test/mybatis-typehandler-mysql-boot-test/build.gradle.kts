plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-extension-starters:mybatis-extension-boot-starter"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.mysql:mysql-connector-j")

	testImplementation("org.testcontainers:mysql")
	testImplementation(project(":spring-testcontainers-support"))
}
