@file:Suppress("LocalVariableName")

import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_SRC_DIR_KOTLIN
import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_TEST_SRC_DIR_KOTLIN
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode.Strict
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin")
    id("org.jetbrains.dokka")
    id("com.github.ben-manes.versions")
    id("com.adarshr.test-logger")
    id("io.gitlab.arturbosch.detekt")
    jacoco
}

group = "io.github.christian-draeger"

val release_version: String by project
version = release_version

repositories {
    mavenCentral()
    // still needed because of detekts transitive dependency to kotlinx-html-jvm
    // see: https://github.com/Kotlin/kotlinx.html/issues/173
    jcenter()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
        vendor.set(JvmVendorSpec.ADOPTOPENJDK)
    }
    withJavadocJar()
    withSourcesJar()
}

kotlin {
    explicitApi = Strict
}

testlogger {
    setTheme("mocha-parallel")
    slowThreshold = 1000
}

jacoco {
    toolVersion = "0.8.6"
}

val detekt_version: String by project

detekt {
    toolVersion = detekt_version
    autoCorrect = true
    input = files(
        DEFAULT_SRC_DIR_KOTLIN,
        DEFAULT_TEST_SRC_DIR_KOTLIN,
    )
    buildUponDefaultConfig = true
    config = files("$rootDir/detekt.yml")
    // ignoreFailures = true
    reports { html { enabled = true } }
}

dependencies {
    implementation(kotlin("stdlib"))
    val kohttpVersion = "0.12.0"
    implementation("io.github.rybalkinsd:kohttp:$kohttpVersion")
    implementation("io.github.rybalkinsd:kohttp-jackson:$kohttpVersion")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.28.0")
    testImplementation("io.mockk:mockk:1.11.0")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.strikt:strikt-core:0.31.0")
    val testcontainersVersion = "1.15.3"
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
    testImplementation("org.testcontainers:postgresql:$testcontainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detekt_version")
}

nexusPublishing {
    repositories {
        sonatype()
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = rootProject.name
            from(components["java"])
            pom {
                name.set("Mobilitybox Kotlin client")
                description.set("A kotlin client for themobilitybox.com")
                url.set("https://github.com/christian-draeger/${rootProject.name}")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("christian-draeger")
                        name.set("Christian Dräger")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/christian-draeger/${rootProject.name}.git")
                    developerConnection.set("scm:git:ssh://github.com:christian-draeger/${rootProject.name}.git")
                    url.set("https://github.com/christian-draeger/${rootProject.name}/tree/main")
                }
            }
        }
    }
    signing {
        sign(publishing.publications["mavenJava"])
        val signingKeyId: String? by project
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    }
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<KotlinCompile> {
        dependsOn(detekt)
        kotlinOptions.apply {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
            apiVersion = "1.5"
            languageVersion = "1.5"
        }
    }

    withType<JacocoReport> {
        reports {
            xml.isEnabled = true
        }
    }

    withType<Test> {
        dependsOn(detekt)
        useJUnitPlatform()
        testLogging {
            events(
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
                TestLogEvent.FAILED
            )
        }
    }
}
