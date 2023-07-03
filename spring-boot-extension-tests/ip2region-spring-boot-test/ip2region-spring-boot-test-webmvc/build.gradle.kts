plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-extension-starters:ip2region-spring-boot-starter"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
}
