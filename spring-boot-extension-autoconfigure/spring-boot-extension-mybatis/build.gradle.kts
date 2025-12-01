description = "spring boot extension mybatis"

dependencies {
	api(project(":spring-extension-context:spring-extension-mybatis"))
	api("org.mybatis.spring.boot:mybatis-spring-boot-autoconfigure")
	optional("org.postgresql:postgresql")
	optional("com.mysql:mysql-connector-j")

	testImplementation("com.zaxxer:HikariCP")
}
