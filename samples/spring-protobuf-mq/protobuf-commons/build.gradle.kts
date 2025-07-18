import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.proto

plugins {
	com.livk.common
	google.protobuf
}

dependencies {
	api(project(":spring-extension-commons"))
	api(libs.protobuf.java)
	api("org.springframework:spring-web")
	api(project(":spring-boot-extension-starters:mapstruct-spring-boot-starter"))
	annotationProcessor("org.mapstruct:mapstruct-processor")
}

sourceSets {
	main {
		proto {
			srcDir("src/main/protobuf")
		}
	}
}

tasks.checkFormatMain {
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
