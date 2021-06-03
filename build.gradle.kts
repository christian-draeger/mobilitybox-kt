@file:Suppress("LocalVariableName")

import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode.Strict

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
version = "1.0-SNAPSHOT"

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
        io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_SRC_DIR_KOTLIN,
        io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_TEST_SRC_DIR_KOTLIN
    )
    buildUponDefaultConfig = true
    config = files("$rootDir/detekt.yml")
    // ignoreFailures = true
    reports { html { enabled = true } }
}

dependencies {
    implementation(kotlin("stdlib"))
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
                description.set("")
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

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
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
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
            )
        }
        systemProperties = mapOf(
            "junit.jupiter.execution.parallel.enabled" to true,
            "junit.jupiter.execution.parallel.mode.default" to "concurrent",
            "junit.jupiter.execution.parallel.mode.classes.default" to "concurrent"
        )
    }
}
