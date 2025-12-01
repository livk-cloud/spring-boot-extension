description = "mybatis plugins spring boot starter"

dependencies {
	api("org.mybatis.spring.boot:mybatis-spring-boot-starter")
	api("com.github.jsqlparser:jsqlparser")
	api(project(":spring-boot-extension-autoconfigure:spring-boot-extension-mybatis"))
}
