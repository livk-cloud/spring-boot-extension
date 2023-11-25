plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-example:spring-protobuf-mq:protobuf-commons"))
	implementation("org.springframework.boot:spring-boot-starter-pulsar")
	implementation("org.springframework.boot:spring-boot-starter-web")
}
