import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	com.livk.service
	alias(libs.plugins.asciidoctor.jvm)
}

configurations {
	create("asciidoctorExt")
}

dependencies {
	implementation(project(":spring-extension-commons"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	add("asciidoctorExt", "org.springframework.restdocs:spring-restdocs-asciidoctor")
}

tasks.withType<Test> {
	useJUnitPlatform()
	outputs.dir(file("build/generated-snippets"))
}

tasks.withType<BootJar> {
	val asciidoctor = tasks.named("asciidoctor").get() as AsciidoctorTask
	dependsOn("asciidoctor")
	from("${asciidoctor.outputDir}/html5") {
		into("static/docs")
	}
}
