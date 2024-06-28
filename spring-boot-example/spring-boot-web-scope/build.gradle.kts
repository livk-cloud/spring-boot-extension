plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-extension-commons"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework:spring-webflux")
	implementation("io.projectreactor.netty:reactor-netty-http")
	implementation("com.squareup.okhttp3:okhttp")
	compileProcessor(project(":spring-auto-service"))
}
