plugins {
    com.livk.service
}

dependencies {
    implementation(project(":spring-boot-extension-starters:mapstruct-spring-boot-starter"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileProcessor("org.mapstruct:mapstruct-processor")
    compileProcessor("org.projectlombok:lombok-mapstruct-binding")
}
