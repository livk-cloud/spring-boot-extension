plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-extension-commons"))
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation(variantOf(libs.clickhouse.r2dbc) {
		classifier("http")
	})
	implementation("org.apache.httpcomponents.client5:httpclient5")

	testImplementation("org.testcontainers:clickhouse")
	testImplementation("org.testcontainers:r2dbc")
	testImplementation(project(":spring-testcontainers-support"))
}
