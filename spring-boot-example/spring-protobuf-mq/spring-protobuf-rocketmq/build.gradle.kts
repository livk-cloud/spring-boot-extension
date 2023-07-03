plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-example:spring-protobuf-mq:protobuf-commons"))
	implementation("org.apache.rocketmq:rocketmq-spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
}
