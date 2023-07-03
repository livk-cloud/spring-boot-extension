plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-extension-commons"))
	implementation(project(":spring-boot-example:spring-netty:netty-commons"))
	implementation("org.springframework.boot:spring-boot-starter-webflux")
}
