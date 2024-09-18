package data

import io.ktor.client.*
import io.ktor.client.plugins.logging.*

actual val client: HttpClient
    get() = HttpClient() {
    install(Logging) {
        level = LogLevel.ALL
    }
    expectSuccess = true
}