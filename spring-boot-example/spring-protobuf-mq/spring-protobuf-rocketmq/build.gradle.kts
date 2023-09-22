plugins {
	com.livk.service
}

dependencies {
	implementation(project(":spring-boot-example:spring-protobuf-mq:protobuf-commons"))
	implementation(libs.rocketmq.boot.starter)
	implementation("org.springframework.boot:spring-boot-starter-web")
}
