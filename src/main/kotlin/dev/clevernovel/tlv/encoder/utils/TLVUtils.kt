package dev.clevernovel.tlv.encoder.utils

import dev.clevernovel.tlv.encoder.dto.CryptoRequest
import dev.clevernovel.tlv.encoder.dto.CryptoResponse
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

object TLVUtils {
    private const val BUFFER_SIZE = 1024
    private const val ZERO_BYTE: Byte = 0
    private const val ONE_BYTE: Byte = 1
    private const val TAG_LENGTH = 1
    private const val INT_SIZE = 4

    fun encodeCryptoRequest(data: CryptoRequest): ByteArray {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE)
        encodeElement(buffer, 1, data.data)
        encodeElement(buffer, 2, data.key)
        encodeElement(buffer, 3, data.iv)
        encodeElement(buffer, 4, data.algorithm.toByteArray(StandardCharsets.UTF_8))
        encodeElement(buffer, 5, if (data.encrypting) byteArrayOf(ONE_BYTE) else byteArrayOf(ZERO_BYTE))
        buffer.put(ZERO_BYTE)
        return buffer.array().sliceArray(0 until buffer.position())
    }

    fun decodeCryptoRequest(encodedData: ByteArray): CryptoRequest {
        val buffer = ByteBuffer.wrap(encodedData)
        val data = decodeElement(buffer)
        val key = decodeElement(buffer)
        val iv = decodeElement(buffer)
        val algorithm = String(decodeElement(buffer), StandardCharsets.UTF_8)
        val encrypting = decodeElement(buffer)[0] == ONE_BYTE
        return CryptoRequest(data, key, iv, algorithm, encrypting)
    }

    fun encodeCryptoResponse(data: CryptoResponse): ByteArray {
        val buffer = ByteBuffer.allocate(BUFFER_SIZE)
        encodeElement(buffer, 1, data.result)
        buffer.put(ZERO_BYTE)
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
        if (buffer.remaining() < TAG_LENGTH) throw IllegalArgumentException("Invalid buffer length")
        val tag = buffer.get()
        if (tag == ZERO_BYTE) return ByteArray(0)
        if (buffer.remaining() < INT_SIZE) throw IllegalArgumentException("Invalid buffer length")
        val length = buffer.int
        if (buffer.remaining() < length) throw IllegalArgumentException("Invalid buffer length")
        val value = ByteArray(length)
        buffer.get(value)
        return value
    }
}
