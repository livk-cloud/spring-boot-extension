plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-extension-commons"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter")
	implementation(group = "com.clickhouse", name = "clickhouse-jdbc", classifier = "http")
	implementation("org.apache.httpcomponents.client5:httpclient5")
}
