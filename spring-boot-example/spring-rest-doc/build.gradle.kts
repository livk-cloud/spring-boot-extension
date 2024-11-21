plugins {
	com.livk.service
	alias(libs.plugins.asciidoctor.jvm)
}

configurations.create("asciidoctorExt")

dependencies {
	implementation(project(":spring-extension-commons"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	add("asciidoctorExt", "org.springframework.restdocs:spring-restdocs-asciidoctor")
}

tasks.test {
	useJUnitPlatform()
	outputs.dir(file("build/generated-snippets"))
}

tasks.jar {
	dependsOn("asciidoctor")
	from("${tasks.asciidoctor.get().outputDir}/html5") {
		into("static/docs")
	}
}
