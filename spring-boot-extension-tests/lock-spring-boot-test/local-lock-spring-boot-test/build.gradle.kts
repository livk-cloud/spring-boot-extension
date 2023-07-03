plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-extension-starters:lock-local-spring-boot-starter"))
	implementation("org.springframework.boot:spring-boot-starter-web")
}
