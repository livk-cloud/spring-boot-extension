plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-extension-starters:mybatis-extension-boot-starter"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.mysql:mysql-connector-j")
	implementation(libs.pagehelper.starter)
	testImplementation("com.h2database:h2")
}
