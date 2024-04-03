import org.asciidoctor.gradle.jvm.AsciidoctorTask

plugins {
	com.livk.root
	alias(libs.plugins.asciidoctor.jvm)
}

configurations {
	create("asciidoctorExtensions")
}

dependencies {
	add("asciidoctorExtensions", libs.spring.asciidoctor)
}

tasks {
	"asciidoctor"(AsciidoctorTask::class) {
		configurations("asciidoctorExtensions")
		baseDirFollowsSourceDir()
		sourceDir(file("./docs/asciidoctor"))
		sources {
			include("index.adoc")
		}
		setOutputDir(file("./docs"))
		outputOptions {
			backends("spring-html")
		}
	}
}

val root = setOf(project(":spring-boot-extension-starters"))
val bom = setOf(project(":spring-extension-bom"), project(":spring-extension-dependencies"))
val module = subprojects.filter { it.buildFile.exists() } - (bom + root)

configure(module) {
	apply(plugin = "com.livk.module")

	dependencies {
		management(platform(project(":spring-extension-dependencies")))
		compileProcessor("org.projectlombok:lombok")
		annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
		annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
		testImplementation("org.springframework:spring-tx")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	}
}

allprojects {
	repositories {
		maven("https://repo.spring.io/release")
		maven("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
	}
}
