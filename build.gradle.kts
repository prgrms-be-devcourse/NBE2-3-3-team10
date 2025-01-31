plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("plugin.serialization") version "1.6.21"
}

group = "org.washcode"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
    }
}


dependencies {

    // Default
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Spring Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Spring OAuth2
    // implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'
    implementation ("org.springframework.boot:spring-boot-starter-security")

    // Valid
    // implementation("org.springframework.boot:spring-boot-starter-validation")

    // MariaDB & Spring JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly ("org.mariadb.jdbc:mariadb-java-client")

    // JWT
    implementation ("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation ("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation ("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Thymeleaf
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // Swagger
    implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    // Test (Junit5)
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Json (Jackson)
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // logger
    implementation ("org.springframework.boot:spring-boot-starter-logging")

    // Feign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // Kafka
    implementation ("org.springframework.kafka:spring-kafka")
    implementation ("org.apache.kafka:kafka-streams")
    implementation ("org.apache.kafka:kafka-clients")

}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
