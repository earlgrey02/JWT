plugins {
    id("io.spring.dependency-management") version "1.1.4"
    id("jacoco")
    id("maven-publish")
    id("org.jetbrains.dokka") version "1.9.20"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
}

group = "com.github"
version = "1.1.1"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:3.2.0")
    implementation("org.springframework:spring-webflux:6.1.2")
    implementation("org.springframework.security:spring-security-core:6.2.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.1")
    testImplementation("io.kotest:kotest-runner-junit5:5.6.2")
    testImplementation("io.kotest:kotest-assertions-core:5.6.2")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")
    testImplementation("com.ninja-squad:springmockk:3.0.1")
}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }

    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }

    jacocoTestCoverageVerification {
        violationRules {
            rule {
                element = "CLASS"

                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = BigDecimal("0.8")
                }

                limit {
                    counter = "METHOD"
                    value = "COVEREDRATIO"
                    minimum = BigDecimal("0.8")
                }

                excludes = listOf(
                    "**.*Configuration*",
                    "**.*Authentication*",
                    "**.*Filter*",
                )
            }
        }
    }

    jacocoTestReport {
        reports {
            html.required.set(true)
            xml.required.set(true)
            finalizedBy(jacocoTestCoverageVerification)
        }
    }

    dokkaHtml {
        version = "1.1.1"
    }
}

jacoco {
    toolVersion = "0.8.8"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github"
            artifactId = "jwt"
            version = "1.1.1"

            from(components["java"])
        }
    }
}
