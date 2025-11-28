plugins {
	com.livk.common
	com.livk.mvn.deployed
	com.livk.jacoco
}

description = "spring commons packages"

dependencies {
	optional("org.springframework:spring-webflux")
	optional("org.springframework:spring-tx")
	optional("org.springframework.boot:spring-boot-starter")
	optional("org.springframework.boot:spring-boot-restclient")
	optional("org.springframework.boot:spring-boot-webclient")

	optional("io.projectreactor.netty:reactor-netty-http")
	optional("com.github.jsqlparser:jsqlparser")
	optional("jakarta.servlet:jakarta.servlet-api")
	optional("com.squareup.okhttp3:okhttp")
	optional("com.googlecode.aviator:aviator")
	optional("org.apache.commons:commons-jexl3")
	optional("org.mvel:mvel2")
	optional("org.freemarker:freemarker")
	optional("io.micrometer:micrometer-tracing")
	optional("net.bytebuddy:byte-buddy")

	api("org.springframework:spring-core")
	api("com.google.guava:guava")
	api("tools.jackson.core:jackson-databind")

	aptCompile(project(":spring-auto-service"))

	testImplementation("io.projectreactor:reactor-test")
	testImplementation("tools.jackson.dataformat:jackson-dataformat-xml")
	testImplementation("tools.jackson.dataformat:jackson-dataformat-yaml")
}
