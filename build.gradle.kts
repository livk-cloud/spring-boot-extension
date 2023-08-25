import org.asciidoctor.gradle.jvm.AsciidoctorTask
import java.io.BufferedReader
import java.io.FileReader

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
		sourceDir(file("./docs"))
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
val bom = setOf(project(":spring-extension-dependencies"))
val gradleModuleProjects = (subprojects.filter { it.buildFile.exists() } - (bom + root))

configure(gradleModuleProjects) {
	apply(plugin = "com.livk.module")

	dependencies {
		management(platform(project(":spring-extension-dependencies")))
		compileProcessor("org.projectlombok:lombok")
		annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
		annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
		testImplementation("org.springframework:spring-tx")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}

	tasks.withType<Test> {
		val exclude = System.getProperty("exclude")
		if (exclude != null && exclude.toBoolean()) {
			val reader = BufferedReader(FileReader("./exclude.txt"))
			val lines: List<String> = reader.readLines().map { it.replace(".", "/") + ".class" }
			exclude(lines)
		}
	}
}

allprojects {
	repositories {
		maven { setUrl("https://repo.spring.io/milestone/") }
		maven { setUrl("https://plugins.gradle.org/m2/") }
		maven { setUrl("https://maven.aliyun.com/repository/public") }
		maven {
			setUrl("http://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
			isAllowInsecureProtocol = true
		}
	}
}
