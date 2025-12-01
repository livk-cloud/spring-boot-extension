description = "spring boot extension useragent"

dependencies {
	optional("com.blueconic:browscap-java")
	optional("nl.basjes.parse.useragent:yauaa")
	optional("jakarta.servlet:jakarta.servlet-api")
	optional("org.springframework:spring-webmvc")
	optional("org.springframework:spring-webflux")

	testImplementation("io.projectreactor:reactor-test")
}
