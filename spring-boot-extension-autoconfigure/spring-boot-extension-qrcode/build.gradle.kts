description = "spring boot extension qrcode"

dependencies {
	api(project(":spring-extension-context:spring-extension-qrcode"))
	optional("org.springframework.boot:spring-boot-webmvc")
	optional("jakarta.servlet:jakarta.servlet-api")
	optional("org.springframework.boot:spring-boot-webflux")
}
