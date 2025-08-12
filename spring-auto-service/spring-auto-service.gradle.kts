plugins {
	com.livk.common
	com.livk.mvn.deployed
	com.livk.jacoco
}

description = "spring boot auto processor EnableAutoConfiguration"

dependencies {
	api("com.google.auto.service:auto-service")
	annotationProcessor("com.google.auto.service:auto-service")
	testImplementation("org.springframework:spring-core-test")
}
