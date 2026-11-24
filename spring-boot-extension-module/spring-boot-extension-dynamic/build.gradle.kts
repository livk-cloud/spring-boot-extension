description = "spring boot extension dynamic"

dependencies {
	api(project(":spring-extension-context:spring-extension-dynamic"))
	api("org.springframework.boot:spring-boot-jdbc")
	optional("ch.qos.logback:logback-classic")

	testImplementation("com.zaxxer:HikariCP")
	testImplementation("com.mysql:mysql-connector-j")
}
