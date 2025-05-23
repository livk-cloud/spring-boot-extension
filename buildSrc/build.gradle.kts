plugins {
	id("java-gradle-plugin")
	alias(libs.plugins.kotlin.jvm)
}

repositories {
	maven("https://repo.spring.io/milestone")
	maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
	mavenCentral()
	gradlePluginPortal()
}

dependencies {
	implementation(libs.spring.boot.plugin)
	implementation(libs.spring.javaformat.plugin)
	implementation(libs.google.protobuf.plugin)
	implementation(libs.osdetector.gradle.plugin)
	implementation(libs.maven.publish.plugin)
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
