plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-example:spring-authorization-server:server-commons"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter")
	implementation("com.mysql:mysql-connector-j")
	implementation("org.springframework.security:spring-security-oauth2-authorization-server")
	testImplementation("com.h2database:h2")
}
