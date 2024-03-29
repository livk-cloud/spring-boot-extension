import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.proto
import io.spring.javaformat.gradle.tasks.CheckFormat

plugins {
	com.livk.common
	alias(libs.plugins.google.protobuf)
}

dependencies {
	api(project(":spring-extension-commons"))
	api(libs.protobuf.java)
	api("org.springframework:spring-web")
	api(project(":spring-boot-extension-starters:mapstruct-spring-boot-starter"))
	annotationProcessor("org.mapstruct:mapstruct-processor")
	//google protobuf插件使用的依赖申明方式是implementation或者compileOnly
	implementation(platform(project(":spring-extension-dependencies")))
}

sourceSets {
	main {
		proto {
			srcDir("src/main/protobuf")
		}
	}
}

tasks.withType<CheckFormat> {
	exclude("com/livk/proto/gen")
}

protobuf {
	protoc {
		artifact = libs.protobuf.java.protoc.get().toString()
	}

	plugins {
		id("grpc") {
			artifact = libs.grpc.java.get().toString()
		}
	}

	generateProtoTasks {
		ofSourceSet("main").forEach {
			it.plugins {
				id("grpc") {}
			}
		}
	}
}
