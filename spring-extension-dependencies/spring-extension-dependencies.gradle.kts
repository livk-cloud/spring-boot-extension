import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
	com.livk.bom
}

description = "spring boot extension dependencies"

dependencies {
	api(platform(SpringBootPlugin.BOM_COORDINATES))
	api(platform(libs.springdoc.openapi.bom))
	api(platform(libs.spring.boot.admin.dependencies))
	constraints {
		api(libs.bundles.pagehelper.all)
		api(libs.bundles.mybatis.all)
		api(libs.bundles.redisson.all)
		api(libs.bundles.clickhouse.all)
		api(libs.bundles.mapstruct.all)
		api(libs.guava)
		api(libs.easyexcel)
		api(libs.spring.pulsar.starter)
		api(libs.spotbugs.annotations)
		api(libs.curator.recipes)
		api(libs.ip2region)
		api(libs.browscap.java)
		api(libs.auto.service)
		api(libs.yauaa)
		api(libs.google.javase)
		api(libs.jsqlparser)
		api(libs.lettucemod)
		api(libs.minio)
		api(libs.dnsjava)
		api(libs.aliyun.oss)
		api(libs.aviator)
		api(libs.commons.jexl3)
		api(libs.mvel2)
		api(libs.disruptor)
		api(project(":spring-extension-commons"))
		api(project(":spring-auto-service"))
		api(project(":spring-boot-extension-autoconfigure"))
		project(":spring-boot-extension-starters").dependencyProject.subprojects {
			api(this)
		}
	}
}
