plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-example:spring-boot-starter:livk-spring-boot-starter"))
	implementation(project(":spring-boot-example:spring-boot-starter:spring-filter-starter"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework:spring-webflux")
	implementation("io.projectreactor.netty:reactor-netty-http")
	implementation("com.squareup.okhttp3:okhttp")
	compileProcessor(project(":spring-auto-service"))
}
