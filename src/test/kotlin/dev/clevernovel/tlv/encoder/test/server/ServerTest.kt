package dev.clevernovel.tlv.encoder.test.server

import dev.clevernovel.tlv.encoder.dto.CryptoRequest
import dev.clevernovel.tlv.encoder.utils.TLVUtils
import dev.clevernovel.tlv.encoder.server.module
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun Application.testModule() {
    module()
}

class ServerTest {
    @Test
    fun testProcessEndpoint() = testApplication {
        application {
            testModule()
        }

        val request = CryptoRequest(
            data = "Hello, World!".toByteArray(),
            key = "1234567812345678".toByteArray(),
            iv = "1234567812345678".toByteArray(),
            algorithm = "AES/CBC/PKCS5Padding",
            encrypting = true
        )
        val encodedRequest = TLVUtils.encodeCryptoRequest(request)

        client.post("/process") {
            contentType(ContentType.Application.Json)
            setBody(encodedRequest)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            val encodedCryptoResponse: ByteArray = bodyAsChannel().toByteArray()
            val cryptoResponse = TLVUtils.decodeCryptoResponse(encodedCryptoResponse)
            assertTrue(cryptoResponse.result.isNotEmpty())
        }
    }
}
