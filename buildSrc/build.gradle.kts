plugins {
	id("java-gradle-plugin")
	alias(libs.plugins.kotlin.jvm)
}

repositories {
	maven("https://repo.spring.io/release")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-gradle-plugin:${libs.versions.spring.boot.get()}")
	implementation("io.spring.javaformat:spring-javaformat-gradle-plugin:${libs.versions.spring.javaformat.get()}")
	implementation("com.google.protobuf:protobuf-gradle-plugin:${libs.versions.google.protobuf.plugins.get()}")
	implementation("com.google.gradle:osdetector-gradle-plugin:${libs.versions.google.osdetector.plugins.get()}")
}

gradlePlugin {
	plugins {
		create("bomPlugin") {
			id = "com.livk.bom"
			implementationClass = "com.livk.boot.BomPlugin"
		}
		create("modulePlugin") {
			id = "com.livk.module"
			implementationClass = "com.livk.boot.ModulePlugin"
		}
		create("commonPlugin") {
			id = "com.livk.common"
			implementationClass = "com.livk.boot.CommonPlugin"
		}
		create("rootProjectPlugin") {
			id = "com.livk.root"
			implementationClass = "com.livk.boot.RootPlugin"
		}
		create("servicePlugin") {
			id = "com.livk.service"
			implementationClass = "com.livk.boot.ServicePlugin"
		}
		create("deployedPlugin") {
			id = "com.livk.mvn.deployed"
			implementationClass = "com.livk.boot.maven.DeployedPlugin"
		}
		create("protobufPlugin") {
			id = "google.protobuf"
			implementationClass = "com.google.protobuf.gradle.ProtobufPlugin"
		}
	}
}

tasks.jar {
	manifest.attributes.putIfAbsent(
		"Created-By",
		"${System.getProperty("java.version")} (${System.getProperty("java.specification.vendor")})"
	)
	manifest.attributes.putIfAbsent("Gradle-Version", GradleVersion.current())
}
