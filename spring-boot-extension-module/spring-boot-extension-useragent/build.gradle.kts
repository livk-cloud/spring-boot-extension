description = "spring boot extension useragent"

dependencies {
	api(project(":spring-extension-context:spring-extension-useragent"))
	optional("com.blueconic:browscap-java")
	optional("nl.basjes.parse.useragent:yauaa")
	optional("org.springframework.boot:spring-boot-webmvc")
	optional("jakarta.servlet:jakarta.servlet-api")
	optional("org.springframework.boot:spring-boot-webflux")
}
