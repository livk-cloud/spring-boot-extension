plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-extension-starters:distributed-lock-boot-starter"))
	implementation("org.springframework.boot:spring-boot-starter-web")
}
