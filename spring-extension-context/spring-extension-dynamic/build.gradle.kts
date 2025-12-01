description = "spring boot extension dynamic"

dependencies {
	api("org.springframework:spring-jdbc")
	api("org.springframework:spring-aop")

	testImplementation("com.zaxxer:HikariCP")
	testImplementation("com.mysql:mysql-connector-j")
}
