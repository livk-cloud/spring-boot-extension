description = "lock local spring boot starter"

dependencies {
	api("org.springframework.boot:spring-boot-starter-aspectj")
	api(project(":spring-boot-extension-autoconfigure:spring-boot-extension-lock"))
}
