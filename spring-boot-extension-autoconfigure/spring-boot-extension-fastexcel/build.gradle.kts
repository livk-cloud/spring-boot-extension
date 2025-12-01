description = "spring boot extension fastexcel"

dependencies {
	api(project(":spring-extension-context:spring-extension-fastexcel"))
	optional("org.springframework.boot:spring-boot-webmvc")
	optional("jakarta.servlet:jakarta.servlet-api")
	optional("org.springframework.boot:spring-boot-webflux")
}
