plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
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

    implementation(libs.quartz)

    implementation("com.soywiz.korge:korlibs-crypto:5.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.5.1")
}