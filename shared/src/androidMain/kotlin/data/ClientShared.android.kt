package data

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*

actual val client: HttpClient
    get() = HttpClient(OkHttp) {
        install(Logging) {
            level = LogLevel.ALL
        }
        expectSuccess = true
    }