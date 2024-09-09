package data

import korlibs.crypto.HasherFactory
import korlibs.crypto.MD5
import korlibs.crypto.SHA1
import korlibs.crypto.SHA256
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.ln
import kotlin.math.pow

@Serializable
class BloomFilter(
    private val expectedInsertions: Int,
    private val falsePositiveProbability: Double,
    private val hashAlgorithm: String
) {
    val numBits: Int
    val numHashFunctions: Int
    val bits: BooleanArray

    init {
        numBits = ceil(-expectedInsertions * ln(falsePositiveProbability) / ln(2.0).pow(2)).toInt()
        numHashFunctions = ceil(ln(2.0) * numBits / expectedInsertions).toInt()
        bits = BooleanArray(numBits)
    }

    fun add(element: ByteArray) {
        for (i in 0 until numHashFunctions) {
            val hash = hash(element, hashAlgorithm) % numBits
            bits[hash] = true
        }
    }

    fun mightContain(element: ByteArray): Boolean {
        for (i in 0 until numHashFunctions) {
            val hash = hash(element, hashAlgorithm) % numBits
            if (!bits[hash]) {
                return false
            }
        }
        return true
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun toByteArray(): ByteArray {
        return ProtoBuf.encodeToByteArray(this)
    }

    private fun hash(element: ByteArray, algorithm: String): Int {
        val hashBytes = getHasher(algorithm).digest(element).bytes

        var hash = 0
        for (b in hashBytes) {
            hash = 31 * hash + b.toInt()
        }
        return hash.absoluteValue
    }

    private fun getHasher(algorithm: String): HasherFactory {
        return when (algorithm) {
            MD5.name -> MD5
            SHA1.name -> SHA1
            SHA256.name -> SHA256
            else -> MD5
        }
    }
}