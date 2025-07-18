plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-extension-commons"))
	implementation(project(":samples:spring-netty:netty-commons"))
	implementation("org.springframework.boot:spring-boot-starter-webflux")
}
