plugins {
    java
    id("org.springframework.boot") version "3.3.9" // ĐÃ SỬA: Dùng phiên bản 3.3.1 ổn định
    id("io.spring.dependency-management") version "1.1.7" // ĐÃ SỬA: Dùng phiên bản tương thích
    id("io.freefair.lombok") version "8.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.api-client:google-api-client:2.2.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("com.google.api-client:google-api-client:2.2.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    
    // ĐÃ XÓA: Dòng "junit-platform-launcher" gây xung đột đã được xóa bỏ hoàn toàn.
    
    implementation("com.turkraft.springfilter:jpa:3.1.7")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    testRuntimeOnly("com.h2database:h2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}