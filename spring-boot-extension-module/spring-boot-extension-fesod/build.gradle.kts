description = "spring boot extension fesod"

dependencies {
	api(project(":spring-extension-context:spring-extension-fesod"))
	optional("org.springframework.boot:spring-boot-webmvc")
	optional("jakarta.servlet:jakarta.servlet-api")
	optional("org.springframework.boot:spring-boot-webflux")
}
