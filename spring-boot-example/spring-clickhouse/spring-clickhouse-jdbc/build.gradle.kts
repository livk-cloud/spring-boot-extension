plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-extension-commons"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter")
	implementation("${libs.clickhouse.jdbc.get()}:http")
	implementation("org.apache.httpcomponents.client5:httpclient5")


	testImplementation("org.testcontainers:clickhouse")
	testImplementation(project(":spring-testcontainers-support"))
}
