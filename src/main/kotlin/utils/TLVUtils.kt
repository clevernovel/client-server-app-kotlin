package utils

import dto.CryptoRequest
import dto.CryptoResponse
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

object TLVUtils {
    fun encodeCryptoRequest(data: CryptoRequest): ByteArray {
        val buffer = ByteBuffer.allocate(1024)
        encodeElement(buffer, 1, data.data)
        encodeElement(buffer, 2, data.key)
        encodeElement(buffer, 3, data.iv)
        encodeElement(buffer, 4, data.algorithm.toByteArray(StandardCharsets.UTF_8))
        encodeElement(buffer, 5, if (data.encrypting) byteArrayOf(1) else byteArrayOf(0))
        buffer.put(0.toByte()) // End tag
        return buffer.array().sliceArray(0 until buffer.position())
    }

    fun decodeCryptoRequest(encodedData: ByteArray): CryptoRequest {
        val buffer = ByteBuffer.wrap(encodedData)
        val data = decodeElement(buffer)
        val key = decodeElement(buffer)
        val iv = decodeElement(buffer)
        val algorithm = String(decodeElement(buffer), StandardCharsets.UTF_8)
        val encrypting = decodeElement(buffer)[0] == 1.toByte()
        return CryptoRequest(data, key, iv, algorithm, encrypting)
    }

    fun encodeCryptoResponse(data: CryptoResponse): ByteArray {
        val buffer = ByteBuffer.allocate(1024)
        encodeElement(buffer, 1, data.result)
        buffer.put(0.toByte()) // End tag
        return buffer.array().sliceArray(0 until buffer.position())
    }

    fun decodeCryptoResponse(encodedData: ByteArray): CryptoResponse {
        val buffer = ByteBuffer.wrap(encodedData)
        val result = decodeElement(buffer)
        return CryptoResponse(result)
    }

    private fun encodeElement(buffer: ByteBuffer, tag: Int, value: ByteArray) {
        buffer.put(tag.toByte())
        buffer.putInt(value.size)
        buffer.put(value)
    }

    private fun decodeElement(buffer: ByteBuffer): ByteArray {
        val tag = buffer.get()
        if (tag == 0.toByte()) return ByteArray(0) // End tag
        val length = buffer.int
        val value = ByteArray(length)
        buffer.get(value)
        return value
    }
}
