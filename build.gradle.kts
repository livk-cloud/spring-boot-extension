plugins {
	com.livk.root
	alias(libs.plugins.asciidoctor.jvm)
}

configurations.create("asciidoctorExtensions")
dependencies.add("asciidoctorExtensions", libs.spring.asciidoctor)

tasks.asciidoctor {
	configurations("asciidoctorExtensions")
	baseDirFollowsSourceDir()
	sourceDir(file("./docs/asciidoctor"))
	sources {
		include("spring-boot-extension.adoc")
	}
	setOutputDir(file("./docs"))
	outputOptions {
		backends("spring-html")
	}
	doLast {
		copy {
			from("./docs/img/banner-logo-copy.svg")
			into("./docs/img")
			rename { "banner-logo.svg" }
		}
		copy {
			from("./docs/spring-boot-extension.html")
			into("./docs")
			rename { "spring-boot-extension-${findProperty("version")}.html" }
		}
		delete("./docs/fonts", "./docs/spring-boot-extension.html")
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

	afterEvaluate {
		dependencies {
			compileOnly(libs.spotbugs.annotations)
		}
	}
}

allprojects {
	repositories {
		maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
//		maven("https://repo.spring.io/release")
		maven("https://repo.spring.io/milestone")
		mavenCentral()
	}
}
