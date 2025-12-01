description = "dynamic spring boot starter"

dependencies {
	api("org.springframework.boot:spring-boot-starter-aspectj")
	api("org.springframework.boot:spring-boot-starter-jdbc")
	api(project(":spring-boot-extension-autoconfigure:spring-boot-extension-dynamic"))
}
