description = "spring boot extension fastexcel"

dependencies {
	api("cn.idev.excel:fastexcel")
	optional("jakarta.servlet:jakarta.servlet-api")
	optional("org.springframework:spring-webmvc")
	optional("org.springframework:spring-webflux")

	testImplementation("io.projectreactor:reactor-test")
}
