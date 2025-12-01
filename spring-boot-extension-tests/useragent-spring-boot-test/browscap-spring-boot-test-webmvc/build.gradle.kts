plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-extension-starters:browscap-spring-boot-starter"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-webmvc-test")
}
