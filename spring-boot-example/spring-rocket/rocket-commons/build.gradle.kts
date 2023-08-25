plugins {
	com.livk.common
}

dependencies {
	api(project(":spring-extension-commons"))
	api(libs.rocketmq.boot.starter)
}
