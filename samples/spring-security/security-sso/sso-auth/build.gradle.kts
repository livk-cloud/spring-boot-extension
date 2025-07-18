plugins {
	com.livk.service
}

dependencies {
	implementation(project(":samples:spring-security:security-sso:sso-commons"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter")
	implementation("com.mysql:mysql-connector-j")
	testImplementation("com.h2database:h2")
}
