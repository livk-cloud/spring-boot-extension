plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-extension-starters:fastexcel-spring-boot-starter"))
	implementation("org.springframework.boot:spring-boot-starter-webflux")
}
