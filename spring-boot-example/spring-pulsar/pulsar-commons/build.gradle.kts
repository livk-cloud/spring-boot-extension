plugins {
	com.livk.common
}

dependencies {
	api(project(":spring-extension-commons"))
	api("org.springframework.pulsar:spring-pulsar-spring-boot-starter")
	api("org.springframework.boot:spring-boot-autoconfigure")
}
