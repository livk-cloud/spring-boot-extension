plugins {
	com.livk.root
	alias(libs.plugins.asciidoctor.jvm)
}

configurations.create("asciidoctorExtensions")
dependencies {
	"asciidoctorExtensions"(libs.spring.asciidoctor)
}

tasks.asciidoctor {
	configurations("asciidoctorExtensions")
	baseDirFollowsSourceDir()
	val docsDir = layout.projectDirectory.dir("docs")
	sourceDir(docsDir.dir("asciidoctor"))
	sources {
		include("spring-boot-extension.adoc")
	}
	setOutputDir(docsDir)
	outputOptions {
		backends("spring-html")
	}
	doLast {
		copy {
			from(docsDir.file("img/banner-logo-copy.svg"))
			into(docsDir.dir("img"))
			rename { "banner-logo.svg" }
		}
		file(docsDir.file("spring-boot-extension.html"))
			.renameTo(file(docsDir.file("spring-boot-extension-${project.version}.html")))
		delete(docsDir.dir("fonts"))
	}
}

val root = setOf(
	project(":spring-boot-extension-autoconfigure"),
	project(":spring-boot-extension-starters"),
	project(":spring-extension-context")
)
val bom = setOf(
	project(":spring-extension-bom"),
	project(":spring-extension-dependencies")
)
val module = subprojects.filter { it.buildFile.exists() } - (bom + root)

configure(module) {
	apply(plugin = "com.livk.module")

	dependencies {
		management(platform(project(":spring-extension-dependencies")))
		aptCompile("org.projectlombok:lombok")
		annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
		annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
		testImplementation("org.springframework:spring-tx")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	}

	afterEvaluate {
		dependencies {
			compileOnly(libs.spotbugs.annotations)
			checkstyle(libs.spring.javaformat.checkstyle) {
				exclude(group = "com.puppycrawl.tools", module = "checkstyle")
			}
			checkstyle(libs.checkstyle)
		}
	}
}

allprojects {
	repositories {
		mavenCentral()
		maven("https://central.sonatype.com/repository/maven-snapshots")
//		国内镜像源，海外CI拉取容易失败，在国内构建项目使用即可
//		maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
	}
}
