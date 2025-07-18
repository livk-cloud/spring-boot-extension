import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.proto

plugins {
	com.livk.common
	google.protobuf
}

dependencies {
	api("io.netty:netty-all")
	api(libs.protobuf.java)
}

tasks.checkFormatMain {
	exclude("com/livk/netty/commons/protobuf")
}

sourceSets {
	main {
		proto {
			srcDir("src/main/protobuf")
		}
	}
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
