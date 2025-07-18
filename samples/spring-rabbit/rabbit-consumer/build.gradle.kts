plugins {
	com.livk.service
}

dependencies {
	implementation(project(":samples:spring-rabbit:rabbit-commons"))
	implementation("org.springframework.boot:spring-boot-starter-web")
}
