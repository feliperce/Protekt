package io.github.feliperce.protekt.job

import data.BloomFilter
import data.MalwareInfo
import extensions.loadBloomFilter
import extensions.saveBloomFilter
import korlibs.crypto.MD5
import korlibs.crypto.md5
import korlibs.encoding.hex
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.files.sink
import kotlinx.io.readByteArray
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.io.File
import kotlin.io.path.*

class UpdateAvDbJob : Job {
    override fun execute(context: JobExecutionContext) {
        val clamAvDbFolder = File("/home/felipe/clamav-db")
        clamAvDbFolder.mkdir()

        val dbPath = File("/home/felipe/Documentos/virus teste/main.hdb")

        val virusPath = Path("/home/felipe/Documentos/virus teste/eicar.com.txt")

        println("JOB START")
        /*execAndWait(
            command = "sigtool -u ~/clamav-db/main.cvd",
            dir = clamAvDbFolder
        )*/

        val malwareInfoList = readMalwareInfoFromFile(dbPath)

        val bloomFilter = BloomFilter(
            expectedInsertions = malwareInfoList.size,
            falsePositiveProbability = 0.01,
            hashAlgorithm = MD5.name
        )

        val virusMd5 = SystemFileSystem.source(virusPath).buffered().readByteArray().md5().hex

        malwareInfoList.forEach {
            println(it)
            bloomFilter.add(it.hash.toByteArray())
        }

        malwareInfoList.forEach {
            if (it.hash == virusMd5.toString()) {
                println("**************************************************************************************************************************************************************")
            }
        }


        println("virus md5: $virusMd5")
        println("Bloom mightcontain: "+bloomFilter.mightContain(virusMd5.toByteArray()))

        //SystemFileSystem.source(virusPath).buffered()

        val binPath = Path("/home/felipe/Documentos/virus teste/aaa.bin")

        binPath.saveBloomFilter(bloomFilter)

        val newBloom = Path("/home/felipe/Documentos/virus teste/aaa.bin").loadBloomFilter()

        println("NEW BLOOM INFO: ${newBloom?.numBits} - ${newBloom?.numHashFunctions}")

        println("NEW FILE Bloom mightcontain: "+newBloom?.mightContain(virusMd5.toByteArray()))
    }

    fun readMalwareInfoFromFile(file: File): List<MalwareInfo> {
        val malwareInfoList = mutableListOf<MalwareInfo>()

        file.forEachLine { line ->
            val parts = line.split(":")
            if (parts.size == 3) {
                val malwareInfo = MalwareInfo(parts[0], parts[1], parts[2])
                malwareInfoList.add(malwareInfo)
            } else {
                println("Linha inválida no arquivo: $line")
            }
        }

        return malwareInfoList
    }
}

fun execAndWait(command: String, dir: File? = null): Int {
    return ProcessBuilder("/bin/sh", "-c", command)
        .redirectErrorStream(true)
        .inheritIO()
        .directory(dir)
        .start()
        .waitFor()
}

// sigtool -u ~/clamav-db/main.cvd