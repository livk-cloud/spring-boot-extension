plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-extension-starters:redis-ops-boot-starter"))
	implementation(project(":spring-boot-extension-starters:distributed-lock-boot-starter"))
	implementation(project(":spring-boot-extension-starters:redisson-spring-boot-starter"))
	implementation("org.springframework.boot:spring-boot-starter-web")
}
