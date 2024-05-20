plugins {
	com.livk.common
	com.livk.mvn.deployed
}

description = "spring boot extension core"

dependencies {
	api(project(":spring-extension-commons"))
	optional("org.springframework:spring-jdbc")
	optional("jakarta.servlet:jakarta.servlet-api")
	optional("jakarta.validation:jakarta.validation-api")
	optional("com.alibaba:easyexcel")
	optional("org.springframework:spring-webmvc")
	optional("org.springframework:spring-webflux")
	optional("dnsjava:dnsjava")
	optional("org.apache.curator:curator-recipes")
	optional("org.mapstruct:mapstruct")
	optional("org.aspectj:aspectjweaver")
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
	optional("com.lmax:disruptor")
	optional("org.redisson:redisson")

	testImplementation("io.lettuce:lettuce-core")
	testImplementation("com.h2database:h2")
	testImplementation("com.mysql:mysql-connector-j")
	testImplementation("org.postgresql:postgresql")
	testImplementation("com.zaxxer:HikariCP")
	testImplementation("io.projectreactor:reactor-test")

	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.testcontainers:mysql")
	testImplementation(project(":spring-extension-testcontainers"))
}
