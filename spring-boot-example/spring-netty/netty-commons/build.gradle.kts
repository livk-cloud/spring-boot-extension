import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.proto

plugins {
	com.livk.common
	alias(libs.plugins.google.protobuf)
}

dependencies {
	api("io.netty:netty-all")
	api(libs.protobuf.java)
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

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:3.24.3"
	}

	plugins {
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:1.58.0"
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
