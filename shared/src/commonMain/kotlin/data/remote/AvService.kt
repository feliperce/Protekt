package data.remote

import SvProperty
import data.client
import extensions.saveBloomFilter
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.utils.io.*
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.write

class AvService (

) {

    suspend fun getMd5Db(onRead: (bytes: ByteArray) -> Unit) {
        client.prepareGet("$BASE_URL/getmd5db").execute { httpResponse ->
            val channel: ByteReadChannel = httpResponse.body()
            while (!channel.isClosedForRead) {
                val packet = channel.readRemaining()
                while (!packet.isEmpty) {
                    val bytes = packet.readBytes()
                    //file.appendBytes(bytes)
                    /*SystemFileSystem.sink(Path(tempFile+"/dasdas.bin"), true).buffered().use { sink ->
                        sink.write(bytes)
                    }*/
                    onRead(bytes)
                    println("Received  bytes from ${httpResponse.contentLength()}")
                }
            }
            //println("A file saved to ${file.path}")
        }

        /*val httpResponse: HttpResponse = client.get("http://192.168.1.9:8080/getmd5db") {
            onDownload { bytesSentTotal, contentLength ->
                println("Received $bytesSentTotal bytes from $contentLength")
            }
        }
        val responseBody: ByteArray = httpResponse.body()

        onRead(responseBody)*/

    }

    companion object {
        const val BASE_URL = "http://${SvProperty.HOST}:${SvProperty.SERVER_PORT}"
    }
}