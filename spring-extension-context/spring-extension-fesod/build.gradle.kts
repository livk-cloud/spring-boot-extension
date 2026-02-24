description = "spring boot extension fesod"

dependencies {
	api("org.apache.fesod:fesod-sheet")
	optional("jakarta.servlet:jakarta.servlet-api")
	optional("org.springframework:spring-webmvc")
	optional("org.springframework:spring-webflux")

	testImplementation("io.projectreactor:reactor-test")
}
