plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-extension-starters:easyexcel-spring-boot-starter"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter")
	implementation("com.mysql:mysql-connector-j")
	testImplementation("com.h2database:h2")
}
