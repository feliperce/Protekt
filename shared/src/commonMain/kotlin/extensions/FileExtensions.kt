package extensions

import data.BloomFilter
import kotlinx.io.IOException
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf


fun Path.saveBloomFilter(bloomFilter: BloomFilter) {
    try {
        SystemFileSystem.sink(this).buffered().use { sink ->
            sink.write(bloomFilter.toByteArray())
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun Path.loadBloomFilter(): BloomFilter? {
    try {
        SystemFileSystem.source(this).buffered().use { source ->
            val bytes = source.readByteArray()
            return ProtoBuf.decodeFromByteArray<BloomFilter>(bytes)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
}