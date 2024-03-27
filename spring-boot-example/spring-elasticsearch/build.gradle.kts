plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-extension-commons"))
	implementation("co.elastic.clients:elasticsearch-java")
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation(project(":spring-extension-testcontainers"))
	testImplementation("org.testcontainers:elasticsearch")
}