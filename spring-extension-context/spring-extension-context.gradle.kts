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
	optional("org.lionsoul:ip2region")
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
	optional("org.apache.commons:commons-pool2")
	optional("com.lmax:disruptor")
	optional("org.redisson:redisson")
}
