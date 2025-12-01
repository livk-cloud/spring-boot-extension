plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-extension-starters:browscap-spring-boot-starter"))
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	testImplementation("org.springframework.boot:spring-boot-webflux-test")
}
