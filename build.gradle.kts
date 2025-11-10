plugins {
	java
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.mediatel"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
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

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

    implementation ("dev.langchain4j:langchain4j-open-ai:1.4.0")
    implementation ("dev.langchain4j:langchain4j:1.1.0")

	implementation ("io.nats:nats-spring-boot-starter:0.6.2+3.5")

    implementation("org.slf4j:slf4j-api:2.0.13")
//    implementation("ch.qos.logback:logback-classic:1.5.6")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
