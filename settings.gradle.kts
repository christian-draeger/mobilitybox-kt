@file:Suppress("LocalVariableName")

rootProject.name = "mobilitybox-kt"

pluginManagement {
    val kotlin_version: String by settings
    repositories {
        gradlePluginPortal()
    }
    plugins {
        kotlin("jvm") version kotlin_version
        id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
        id("org.jetbrains.dokka") version "1.4.30"
        id("com.adarshr.test-logger") version "3.0.0"
        jacoco
        `java-library`
        `maven-publish`
        signing
        id("com.github.ben-manes.versions") version "0.38.0"
        id("io.gitlab.arturbosch.detekt") version "1.17.1"
    }
}
