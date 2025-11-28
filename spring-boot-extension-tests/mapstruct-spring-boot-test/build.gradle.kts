plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-extension-starters:mapstruct-spring-boot-starter"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	aptCompile("org.mapstruct:mapstruct-processor")
	aptCompile("org.projectlombok:lombok-mapstruct-binding")
	testImplementation("org.springframework.boot:spring-boot-webmvc-test")
}
