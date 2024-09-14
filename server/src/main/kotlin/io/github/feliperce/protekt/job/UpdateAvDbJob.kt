package io.github.feliperce.protekt.job

import SvProperty.MD5_BF_FILE_NAME
import SvProperty.SHA1_BF_FILE_NAME
import SvProperty.SHA256_BF_FILE_NAME
import data.BloomFilter
import data.MalwareInfo
import extensions.saveBloomFilter
import korlibs.crypto.MD5
import korlibs.crypto.SHA1
import korlibs.crypto.SHA256
import kotlinx.io.files.Path
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.io.File

class UpdateAvDbJob : Job {

    private val AV_DB_PATH = "/home/felipe/clamav-db/"
    private val CLAMAV_PATH = "/var/lib/clamav/"
    private val TMP_PATH = "/tmp/"

    override fun execute(context: JobExecutionContext) {
        val clamAvDbFolder = File(AV_DB_PATH)
        clamAvDbFolder.mkdir()

        val dbTmp = File("${TMP_PATH}clamav-db")
        dbTmp.mkdir()

        println("AV UPDATE START")

        execAndWait("sigtool -u ${CLAMAV_PATH}main.cvd", dbTmp)
        execAndWait("cp -rf main.hdb ${clamAvDbFolder.absolutePath}", dbTmp)
        execAndWait("cp -rf main.hsb ${clamAvDbFolder.absolutePath}", dbTmp)

        val mainHdbPath = File("${AV_DB_PATH}main.hdb")
        val mainHsbPath = File("${AV_DB_PATH}main.hsb")

        val mainHdbMalwareInfoList = readMalwareInfoFromFile(mainHdbPath)
        val mainHsbMalwareInfoList = readMalwareInfoFromFile(mainHsbPath)

        val fullHashList = (mainHdbMalwareInfoList + mainHsbMalwareInfoList)

        val hashMd5MalwareInfoList = fullHashList.filter {
            it.hashType == MD5.name
        }
        val hashSha1MalwareInfoList = fullHashList.filter {
            it.hashType == SHA1.name
        }
        val hashSha256MalwareInfoList = fullHashList.filter {
            it.hashType == SHA256.name
        }

        val md5BloomFilter = BloomFilter(
            expectedInsertions = hashMd5MalwareInfoList.size,
            falsePositiveProbability = 0.01,
            hashAlgorithm = MD5.name
        )
        val sha256BloomFilter = BloomFilter(
            expectedInsertions = hashSha256MalwareInfoList.size,
            falsePositiveProbability = 0.01,
            hashAlgorithm = SHA256.name
        )
        val sha1BloomFilter = BloomFilter(
            expectedInsertions = hashSha1MalwareInfoList.size,
            falsePositiveProbability = 0.01,
            hashAlgorithm = SHA1.name
        )

        hashMd5MalwareInfoList.forEach {
            md5BloomFilter.add(it.hash.toByteArray())
        }
        hashSha256MalwareInfoList.forEach {
            sha256BloomFilter.add(it.hash.toByteArray())
        }
        hashSha1MalwareInfoList.forEach {
            sha1BloomFilter.add(it.hash.toByteArray())
        }

        val md5binPath = Path("${AV_DB_PATH}${MD5_BF_FILE_NAME}")
        val sha256binPath = Path("${AV_DB_PATH}${SHA256_BF_FILE_NAME}")
        val sha1binPath = Path("${AV_DB_PATH}${SHA1_BF_FILE_NAME}")

        md5binPath.saveBloomFilter(md5BloomFilter)
        sha256binPath.saveBloomFilter(sha256BloomFilter)
        sha1binPath.saveBloomFilter(sha1BloomFilter)
    }

    fun readMalwareInfoFromFile(file: File): List<MalwareInfo> {
        val malwareInfoList = mutableListOf<MalwareInfo>()

        file.forEachLine { line ->
            val parts = line.split(":")

                val hash = parts[0]
                val hashType = when (hash.length) {
                    32 -> MD5.name
                    40 -> SHA1.name
                    64 -> SHA256.name
                    else -> "undefined"
                }
                val malwareInfo = MalwareInfo(
                    hash = hash,
                    hashType = hashType,
                    id = parts[1],
                    name = parts[2]
                )
                malwareInfoList.add(malwareInfo)

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
