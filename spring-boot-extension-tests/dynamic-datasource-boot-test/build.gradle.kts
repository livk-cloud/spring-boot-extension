plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-extension-starters:dynamic-datasource-boot-starter"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter")
	implementation("com.mysql:mysql-connector-j")
	implementation("org.postgresql:postgresql")

	testImplementation("org.testcontainers:testcontainers-postgresql")
	testImplementation("org.testcontainers:testcontainers-mysql")
	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("org.springframework.boot:spring-boot-webmvc-test")
}
