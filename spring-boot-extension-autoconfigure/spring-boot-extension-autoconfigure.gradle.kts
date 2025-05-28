plugins {
	com.livk.common
	com.livk.mvn.deployed
}

description = "extension spring boot autoconfigure"

dependencies {
	api(project(":spring-extension-context"))
	api("org.springframework.boot:spring-boot-autoconfigure")
	optional("org.springframework.boot:spring-boot-actuator-autoconfigure")
	optional("org.springframework:spring-jdbc")
	optional("jakarta.servlet:jakarta.servlet-api")
	optional("jakarta.validation:jakarta.validation-api")
	optional("cn.idev.excel:fastexcel")
	optional("org.springframework:spring-webmvc")
	optional("org.springframework:spring-webflux")
	optional("org.redisson:redisson")
	optional("org.redisson:redisson-spring-data-35")
	optional("org.apache.curator:curator-recipes")
	optional("org.mapstruct:mapstruct")
	optional("org.aspectj:aspectjweaver")
	optional("org.mybatis.spring.boot:mybatis-spring-boot-autoconfigure")
	optional("org.mybatis:mybatis-spring")
	optional("org.mybatis:mybatis")
	optional("com.github.jsqlparser:jsqlparser")
	optional("com.mysql:mysql-connector-j")
	optional("org.postgresql:postgresql")
	optional("com.google.zxing:javase")
	optional("org.springframework.data:spring-data-redis")
	optional("com.blueconic:browscap-java")
	optional("nl.basjes.parse.useragent:yauaa")
	optional("com.redis:lettucemod")
	optional("org.apache.commons:commons-pool2")
	optional("io.minio:minio")
	optional("com.lmax:disruptor")
	compileProcessor(project(":spring-auto-service"))

	testImplementation(project(":spring-testcontainers-support"))
	testImplementation("com.zaxxer:HikariCP")
	testImplementation("com.redis:testcontainers-redis")
}
