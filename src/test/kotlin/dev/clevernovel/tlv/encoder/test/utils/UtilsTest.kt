package dev.clevernovel.tlv.encoder.utils

import dev.clevernovel.tlv.encoder.dto.CryptoRequest
import dev.clevernovel.tlv.encoder.dto.CryptoResponse
import org.junit.Test
import kotlin.test.assertEquals

class UtilsTest {
    @Test
    fun testCryptoUtils() {
        val data = "Hello, World!".toByteArray()
        val key = "1234567812345678".toByteArray()
        val iv = "1234567812345678".toByteArray()
        val algorithm = "AES/CBC/PKCS5Padding"

        val encryptedData = CryptoUtils.process(data, key, iv, algorithm, true)
        val decryptedData = CryptoUtils.process(encryptedData, key, iv, algorithm, false)

        assertEquals(String(data), String(decryptedData))
    }

    @Test
    fun testTLVUtils() {
        val request = CryptoRequest(
            data = "Hello, World!".toByteArray(),
            key = "1234567812345678".toByteArray(),
            iv = "1234567812345678".toByteArray(),
            algorithm = "AES/CBC/PKCS5Padding",
            encrypting = true
        )

        val encodedRequest = TLVUtils.encodeCryptoRequest(request)
        val decodedRequest = TLVUtils.decodeCryptoRequest(encodedRequest)

        assertEquals(request.data.toList(), decodedRequest.data.toList())
        assertEquals(request.key.toList(), decodedRequest.key.toList())
        assertEquals(request.iv.toList(), decodedRequest.iv.toList())
        assertEquals(request.algorithm, decodedRequest.algorithm)
        assertEquals(request.encrypting, decodedRequest.encrypting)

        val response = CryptoResponse("EncryptedData".toByteArray())
        val encodedResponse = TLVUtils.encodeCryptoResponse(response)
        val decodedResponse = TLVUtils.decodeCryptoResponse(encodedResponse)

        assertEquals(response.result.toList(), decodedResponse.result.toList())
    }
}
