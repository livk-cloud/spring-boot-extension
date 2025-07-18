plugins {
	com.livk.service
}

dependencies {
	implementation(project(":samples:spring-crypto:crypto-commons"))
	implementation("org.springframework.boot:spring-boot-starter-web")
}
