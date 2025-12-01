description = "spring boot extension qrcode"

dependencies {
	api("com.google.zxing:javase")
	optional("jakarta.servlet:jakarta.servlet-api")
	optional("org.springframework:spring-webmvc")
	optional("org.springframework:spring-webflux")

	testImplementation("io.projectreactor:reactor-test")
}
