plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-extension-starters:redis-spring-boot-starter"))
	implementation(project(":spring-boot-extension-starters:lock-redis-spring-boot-starter"))
	implementation("org.springframework.boot:spring-boot-starter-web")
}
