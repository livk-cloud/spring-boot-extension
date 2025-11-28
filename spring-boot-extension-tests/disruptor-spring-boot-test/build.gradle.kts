plugins {
	com.livk.service
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation(project(":spring-boot-extension-starters:disruptor-spring-boot-starter"))
	testImplementation("org.springframework.boot:spring-boot-webmvc-test")
}
