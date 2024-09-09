package extensions

import data.BloomFilter
import data.MalwareInfo
import kotlinx.io.IOException
import kotlinx.io.buffered
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlinx.io.readLine
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf



/*
fun File.saveBloomFilter(bloomFilter: BloomFilter) {
    ObjectOutputStream(FileOutputStream(this)).use { it.writeObject(bloomFilter) }
}

fun File.loadBloomFilter(): BloomFilter {
    ObjectInputStream(FileInputStream(this)).use { return it.readObject() as BloomFilter }
}*/



fun Path.saveBloomFilter(bloomFilter: BloomFilter) {
    try {
        val tobyte = bloomFilter.toByteArray()

        /*val deode = ProtoBuf.decodeFromByteArray<BloomFilter>(tobyte)
        println("DECODED: ${deode.numBits} - ${deode.numHashFunctions}")*/
        /*SystemFileSystem.sink(this).buffered().use { sink ->
            sink.write(bloomFilter.toByteArray()) // Assuming your BloomFilter has a toByteArray() method
        }*/
    } catch (e: IOException) {
        // Handle potential IOExceptions (e.g., file not found, permissions, etc.)
        e.printStackTrace()
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun Path.loadBloomFilter(): BloomFilter? {
    try {
        SystemFileSystem.source(this).buffered().use { source ->
            val bytes = source.readByteArray()

            /*val aa = BinaryFormat<BloomFilter>()

            aa.decodeFromByteArray()*/
            return ProtoBuf.decodeFromByteArray<BloomFilter>(bytes)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
}