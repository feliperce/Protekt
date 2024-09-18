plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
    kotlin("plugin.serialization") version "2.0.0"
}

group = "io.github.feliperce.protekt"
version = "1.0.0"
application {
    mainClass.set("io.github.feliperce.protekt.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)

    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.server.content.negotiation)

    implementation(libs.quartz)

    implementation(libs.korlibs.crypto)
    implementation(libs.kotlinx.io.core)
}