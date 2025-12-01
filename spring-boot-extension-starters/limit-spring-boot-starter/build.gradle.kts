description = "limit spring boot starter"

dependencies {
	api("org.springframework.boot:spring-boot-starter-aspectj")
	api(project(":spring-boot-extension-starters:redisson-spring-boot-starter"))
	api(project(":spring-boot-extension-autoconfigure:spring-boot-extension-limit"))
}
