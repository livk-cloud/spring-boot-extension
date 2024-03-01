plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-extension-commons"))
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("com.clickhouse:clickhouse-r2dbc")
	implementation("com.clickhouse:clickhouse-client")
	implementation("org.apache.httpcomponents.client5:httpclient5")

	testImplementation("org.testcontainers:clickhouse")
	testImplementation("org.testcontainers:r2dbc")
	testImplementation(group = "com.clickhouse", name = "clickhouse-jdbc", classifier = "http")
	testImplementation(project(":spring-extension-testcontainers"))
}
