import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.proto
import io.spring.javaformat.gradle.tasks.CheckFormat

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

tasks.withType<CheckFormat> {
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
