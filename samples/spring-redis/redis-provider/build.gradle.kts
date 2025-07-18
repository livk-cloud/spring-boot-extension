plugins {
	com.livk.service
}

dependencies {
	implementation(project(":samples:spring-redis:redis-commons"))
	implementation("org.springframework.boot:spring-boot-starter-webflux")
}
