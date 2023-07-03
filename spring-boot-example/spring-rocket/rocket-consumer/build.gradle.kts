plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-example:spring-rocket:rocket-commons"))
	implementation("org.springframework.boot:spring-boot-starter-web")
}
