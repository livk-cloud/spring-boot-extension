plugins {
	com.livk.common
}

dependencies {
	api(project(":spring-extension-commons"))
	optional("org.springframework.boot:spring-boot-starter-web")
	optional("org.springframework.boot:spring-boot-starter-webflux")
	compileProcessor(project(":spring-auto-service"))
}
